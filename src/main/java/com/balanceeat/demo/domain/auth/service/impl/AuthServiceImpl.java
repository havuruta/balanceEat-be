package com.balanceeat.demo.domain.auth.service.impl;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.auth.dto.LoginRequestDTO;
import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.auth.dto.TokenDTO;
import com.balanceeat.demo.domain.auth.exception.AuthenticationException;
import com.balanceeat.demo.domain.auth.exception.UserAlreadyExistsException;
import com.balanceeat.demo.domain.auth.jwt.CookieFactory;
import com.balanceeat.demo.domain.auth.jwt.RedisRefreshTokenRepository;
import com.balanceeat.demo.domain.auth.jwt.TokenBlacklist;
import com.balanceeat.demo.domain.auth.jwt.TokenProvider;
import com.balanceeat.demo.domain.auth.service.AuthService;
import com.balanceeat.demo.domain.auth.util.AccountLockUtil;
import com.balanceeat.demo.domain.auth.util.SecurityUtil;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.entity.RefreshToken;
import com.balanceeat.demo.domain.user.entity.User;
import com.balanceeat.demo.domain.user.exception.UserNotFoundException;
import com.balanceeat.demo.domain.user.mapper.RegisterConverter;
import com.balanceeat.demo.domain.user.mapper.UserMapper;
import com.balanceeat.demo.domain.user.service.UserService;
import com.balanceeat.demo.exception.BusinessException;
import com.balanceeat.demo.exception.ErrorMessage;
import com.balanceeat.demo.exception.auth.InvalidTokenException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

import io.jsonwebtoken.Claims;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RegisterConverter registerConverter;
    private final UserService userService;
    private final RedisRefreshTokenRepository refreshTokenRepository;
    private final AccountLockUtil accountLockUtil;
    private final CookieFactory cookieFactory;
    private final TokenBlacklist tokenBlacklist;
    private final TokenProvider tokenProvider;

    @Override
    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        log.debug("회원가입 처리 시작: {}", registerRequestDTO.getEmail());
        log.debug("회원가입 DTO 정보: email={}, nickname={}, gender={}", 
            registerRequestDTO.getEmail(), 
            registerRequestDTO.getNickname(), 
            registerRequestDTO.getGender());
            
        // 1. 이메일 중복 체크
        if (userService.existsByEmail(registerRequestDTO.getEmail())) {
            log.warn("회원가입 실패 - 이미 존재하는 아이디: {}", registerRequestDTO.getEmail());
            throw new UserAlreadyExistsException();
        }
        // 2. 이메일 형식 검증
        if (!SecurityUtil.isValidEmail(registerRequestDTO.getEmail())) {
            log.warn("회원가입 실패 - 유효하지 않은 이메일 형식: {}", registerRequestDTO.getEmail());
            throw new IllegalArgumentException(ErrorMessage.INVALID_EMAIL_FORMAT);
        }
        // 3. 비밀번호 유효성 검사
        if (!SecurityUtil.isValidPassword(registerRequestDTO.getPassword())) {
            log.warn("회원가입 실패 - 유효하지 않은 비밀번호 형식");
            throw new IllegalArgumentException(ErrorMessage.INVALID_PASSWORD_FORMAT);
        }
        // TODO: 테스트 후 주석 해제
        // if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getPasswordConfirm())) {
        //     log.warn("회원가입 실패 - 비밀번호 불일치: {}", registerRequestDTO.getPassword(), registerRequestDTO.getPasswordConfirm());
        //     throw new IllegalArgumentException(ErrorMessage.CONFIRMED_PASSWORD_IS_NOT_SAME);
        // }
        User user = registerConverter.toEntity(registerRequestDTO, passwordEncoder);
        log.debug("변환된 User 엔티티: {}", user);
        
        userMapper.insert(user);
        log.debug("회원가입 처리 완료: {}", user.getEmail());
        
        return registerConverter.toUserResponseDTO(user);
    }
    
    @Override
    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse) {
        log.debug("로그인 처리 시작: {}", loginRequestDTO.getEmail());
        User user = userService.findByEmail(loginRequestDTO.getEmail());

        if (!user.isActive()) {
            log.warn("로그인 실패 - 탈퇴한 회원: {}", loginRequestDTO.getEmail());
            throw new BusinessException(ErrorMessage.USER_ACCOUNT_DISABLED, HttpStatus.BAD_REQUEST);
        }
        
        try {
            // 2. 인증 (UserPrincipal 생성·주입까지 자동 수행)
            UserPrincipal userPrincipal = UserPrincipal.create(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            
            // 3. 로그인 성공 처리
            accountLockUtil.handleLoginSuccess(user);   // ← userRepository.save(user) 생략 가능 (TX)
            
            // 4. 토큰 생성
            TokenDTO.Response tokens = tokenProvider.generateToken(authentication);
            
            // 5. 리프레시 토큰 갱신
            refreshTokenRepository.deleteByKey(userPrincipal.getEmail());
            refreshTokenRepository.save(
                userPrincipal.getEmail(), tokens.getRefreshToken(),
                tokenProvider.getRefreshTokenExpirationTime());
            
            // 6. 쿠키 설정
            cookieFactory.addAccessCookie(httpServletResponse, tokens.getAccessToken());
            cookieFactory.addRefreshCookie(httpServletResponse, tokens.getRefreshToken());
            
            // 7. 사용자 프로필 정보 응답 헤더에 추가
            UserProfileDTO userProfile = userService.getUserProfile(user.getId());
            String userProfileJson = new ObjectMapper().writeValueAsString(userProfile);
            httpServletResponse.setHeader("X-User-Profile", Base64.getEncoder().encodeToString(userProfileJson.getBytes("UTF-8")));
            
            log.debug("로그인 처리 완료: {}", user.getEmail());
            
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            throw new AuthenticationException("로그인 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * 로그아웃 처리
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키에서 토큰 추출
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TokenProvider.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        
        if (accessToken != null) {
            // 2. 액세스 토큰으로부터 인증 정보 추출
            Authentication authentication = tokenProvider.getAuthentication(accessToken, request);
            
            // 3. 리프레시 토큰 삭제
            refreshTokenRepository.deleteByKey(authentication.getName());
            
            // 4. 액세스 토큰을 블랙리스트에 추가
            tokenBlacklist.addToBlacklist(accessToken, tokenProvider.getAccessTokenExpirationTime());
        }
        
        // 5. 쿠키 만료 처리
        cookieFactory.expireAllCookies(response);
    }
    
    @Override
    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        log.debug("토큰 재발급 시작");
        
        // 1. 쿠키에서 리프레시 토큰 추출
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        log.info("reissue 요청의 모든 쿠키: {}", Arrays.toString(cookies));
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("쿠키 검사 - 이름: {}, 값: {}", cookie.getName(), cookie.getValue());
                if (TokenProvider.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    log.info("리프레시 토큰 추출 성공: {}", refreshToken);
                    break;
                }
            }
        }
        
        if (refreshToken == null) {
            log.warn("리프레시 토큰이 없습니다.");
            throw new InvalidTokenException();
        }
        
        // 2. 리프레시 토큰 유효성 검증
        validateRefreshToken(refreshToken);
        
        // 3. 리프레시 토큰에서 사용자 정보 추출
        Claims claims = tokenProvider.parseClaims(refreshToken);
        String email = claims.get(TokenProvider.USER_EMAIL, String.class);
        log.info("리프레시 토큰에서 추출한 이메일: {}", email);
        
        if (email == null) {
            log.warn("리프레시 토큰에서 이메일을 추출할 수 없습니다.");
            throw new InvalidTokenException();
        }
        
        // 4. Redis에서 저장된 토큰 조회 및 검증
        String storedToken = refreshTokenRepository.findByKey(email)
            .map(RefreshToken::getValue)
            .orElse(null);
            
        if (storedToken == null) {
            log.warn("Redis에 저장된 토큰이 없습니다. email: {}", email);
            throw new InvalidTokenException();
        }
        
        // 토큰의 클레임을 비교
        Claims storedClaims = tokenProvider.parseClaims(storedToken);
        Claims providedClaims = tokenProvider.parseClaims(refreshToken);
        
        if (!storedClaims.get(TokenProvider.USER_EMAIL).equals(providedClaims.get(TokenProvider.USER_EMAIL)) ||
            !storedClaims.get(TokenProvider.USER_ID).equals(providedClaims.get(TokenProvider.USER_ID))) {
            log.warn("Redis에 저장된 토큰과 일치하지 않습니다. email: {}, storedToken: {}, providedToken: {}", 
                email, storedToken, refreshToken);
            throw new InvalidTokenException();
        }
        
        // 5. 사용자 정보로 인증 객체 생성
        User user = userMapper.findByEmail(email)
            .orElseThrow(() -> {
                log.error("사용자를 찾을 수 없습니다. email: {}", email);
                return new UserNotFoundException();
            });
            
        if (!user.isActive()) {
            log.warn("비활성화된 사용자입니다. email: {}", email);
            throw new BusinessException(ErrorMessage.USER_ACCOUNT_DISABLED, HttpStatus.BAD_REQUEST);
        }
        
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, null, userPrincipal.getAuthorities());
        
        // 6. 이전 리프레시 토큰 삭제
        refreshTokenRepository.deleteByKey(email);
        
        // 7. 새로운 토큰 생성
        TokenDTO.Response tokenResponse = tokenProvider.generateToken(authentication);
        
        // 8. 새로운 리프레시 토큰 저장

        refreshTokenRepository.save(
            authentication.getName(),
            tokenResponse.getRefreshToken(),
            tokenProvider.getRefreshTokenExpirationTime()
        );
        
        // 9. 새로운 쿠키 설정
        cookieFactory.addAccessCookie(response, tokenResponse.getAccessToken());
        cookieFactory.addRefreshCookie(response, tokenResponse.getRefreshToken());
        
        log.info("토큰 재발급 성공. email: {}", email);
    }
    
    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            log.warn("리프레시 토큰이 유효하지 않습니다.");
            throw new InvalidTokenException();
        }

        Claims claims = tokenProvider.parseClaims(refreshToken);
    }
    
    private String getRefreshToken(String memberId) {
        return refreshTokenRepository.findByKey(memberId)
            .map(RefreshToken::getValue)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.USER_ALREADY_LOGOUT));
    }
    
    private void validateTokenMatch(String storedToken, String providedToken) {
        if (!storedToken.equals(providedToken)) {
            throw new InvalidTokenException();
        }
    }
}

