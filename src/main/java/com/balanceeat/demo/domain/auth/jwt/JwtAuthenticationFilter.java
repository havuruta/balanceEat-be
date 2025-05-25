package com.balanceeat.demo.domain.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        log.info("JwtAuthenticationFilter 시작 - 요청 URI: {}", requestURI);

        // 토큰 재발급 요청은 이 필터를 건너뜁니다
        if (requestURI.equals("/auth/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);
            log.info("추출된 JWT 토큰: {}", jwt);

            if (StringUtils.hasText(jwt)) {
                log.info("토큰 검증 시작");
                if (tokenProvider.validateToken(jwt)) {
                    log.info("토큰 검증 성공");
                    Authentication authentication = tokenProvider.getAuthentication(jwt, request);
                    log.info("생성된 Authentication: {}", authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("토큰 검증 실패");
                }
            } else {
                log.warn("요청에 토큰이 없습니다");
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                log.warn("요청에 쿠키가 없습니다");
                return null;
            }

            log.info("요청의 모든 쿠키: {}", Arrays.toString(cookies));
            log.info("요청 헤더: {}", Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                    headerName -> headerName,
                    request::getHeader
                )));

            Optional<Cookie> accessTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> {
                    boolean matches = TokenProvider.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName());
                    log.info("쿠키 검사 - 이름: {}, 일치여부: {}", cookie.getName(), matches);
                    return matches;
                })
                .findFirst();

            if (accessTokenCookie.isPresent()) {
                String token = accessTokenCookie.get().getValue();
                if (StringUtils.hasText(token)) {
                    log.info("쿠키에서 추출한 토큰: {}", token);
                    return token;
                } else {
                    log.warn("access_token 쿠키의 값이 비어있습니다");
                }
            } else {
                log.warn("access_token 쿠키를 찾을 수 없습니다. 찾는 쿠키 이름: {}", TokenProvider.ACCESS_TOKEN_COOKIE_NAME);
            }
        } catch (Exception e) {
            log.error("쿠키에서 토큰을 추출하는 중 오류 발생", e);
        }
        return null;
    }
}
