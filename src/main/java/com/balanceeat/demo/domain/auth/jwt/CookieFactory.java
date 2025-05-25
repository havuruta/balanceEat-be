package com.balanceeat.demo.domain.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO: 프로필별 설정
 * 1. application-dev.yml: 개발 환경 설정
 *    - spring.profiles.active: dev
 *    - secure: false (HTTP 허용)
 * 
 * 2. application-prod.yml: 운영 환경 설정
 *    - spring.profiles.active: prod
 *    - secure: true (HTTPS만 허용)
 * 
 * 3. 배포 시 application-prod.yml 설정 필요
 */
@Component
public class CookieFactory {

    private static final String SAME_SITE_ATTRIBUTE = "SameSite";
    private static final String SAME_SITE_NONE = "None";
    private static final String SAME_SITE_LAX = "Lax";
    private static final String PATH_VALUE = "/";

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    private boolean isSecure() {
        return !"dev".equals(activeProfile);
    }

    private String getSameSiteValue() {
        return isSecure() ? SAME_SITE_NONE : SAME_SITE_LAX;
    }

    public void addAccessCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(TokenProvider.ACCESS_TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(isSecure())
                .path("/")
                .sameSite(getSameSiteValue())
                .maxAge(TokenProvider.ACCESS_TOKEN_EXPIRE_TIME / 1000)
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void addRefreshCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(TokenProvider.REFRESH_TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(isSecure())
                .path("/")
                .sameSite(getSameSiteValue())
                .maxAge(TokenProvider.REFRESH_TOKEN_EXPIRE_TIME / 1000)
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from(TokenProvider.ACCESS_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(isSecure())
                .path(contextPath + PATH_VALUE)
                .sameSite(getSameSiteValue())
                .maxAge(0)
                .build();
        
        ResponseCookie refreshCookie = ResponseCookie.from(TokenProvider.REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(isSecure())
                .path(contextPath + PATH_VALUE)
                .sameSite(getSameSiteValue())
                .maxAge(0)
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    /**
     * 쿠키를 만료시키는 메서드
     * @param response HTTP 응답 객체
     * @param cookieName 만료시킬 쿠키 이름
     */
    public void expireCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .path(contextPath + PATH_VALUE)
                .maxAge(0)
                .httpOnly(true)
                .secure(isSecure())
                .sameSite(getSameSiteValue())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * 모든 인증 관련 쿠키를 만료시키는 메서드
     * @param response HTTP 응답 객체
     */
    public void expireAllCookies(HttpServletResponse response) {
        expireCookie(response, TokenProvider.ACCESS_TOKEN_COOKIE_NAME);
        expireCookie(response, TokenProvider.REFRESH_TOKEN_COOKIE_NAME);
    }
} 