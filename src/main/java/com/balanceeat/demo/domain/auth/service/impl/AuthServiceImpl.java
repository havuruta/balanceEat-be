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
        User user = userMapper.findByEmail(loginRequestDTO.getEmail())
            .orElseThrow(UserNotFoundException::new);
        
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
            refreshTokenRepository.deleteByKey(authentication.getName());
            refreshTokenRepository.save(
                authentication.getName(), tokens.getRefreshToken(),
                tokenProvider.getRefreshTokenExpirationTime());
            
            // 6. 쿠키 설정
            cookieFactory.addAccessCookie(httpServletResponse, tokens.getAccessToken());
            cookieFactory.addRefreshCookie(httpServletResponse, tokens.getRefreshToken());
            
            log.debug("로그인 처리 완료: {}", user.getEmail());
            
        } catch (AuthenticationException ex) {
            accountLockUtil.handleLoginFailure(user);   // 실패 카운팅
            throw new BadCredentialsException(ErrorMessage.INVALID_PASSWORD_FORMAT);
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
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TokenProvider.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        
        if (refreshToken == null) {
            log.warn("리프레시 토큰이 없습니다");
            throw new InvalidTokenException();
        }
        
        // 2. 리프레시 토큰 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            log.warn("유효하지 않은 리프레시 토큰입니다");
            throw new InvalidTokenException();
        }
        
        // 3. Redis에서 리프레시 토큰 확인
        RefreshToken storedToken = refreshTokenRepository.findByKey(refreshToken)
            .orElseThrow(InvalidTokenException::new);
        
        // 4. 새로운 토큰 생성
        Authentication authentication = tokenProvider.getAuthentication(refreshToken, request);
        TokenDTO.Response tokens = tokenProvider.generateToken(authentication);
        
        // 5. Redis에 새로운 리프레시 토큰 저장
        refreshTokenRepository.deleteByKey(refreshToken);
        refreshTokenRepository.save(
            authentication.getName(),
            tokens.getRefreshToken(),
            tokenProvider.getRefreshTokenExpirationTime()
        );
        
        // 6. 새로운 토큰을 쿠키에 설정
        cookieFactory.addAccessCookie(response, tokens.getAccessToken());
        cookieFactory.addRefreshCookie(response, tokens.getRefreshToken());
        
        log.debug("토큰 재발급 완료");
    }
}

