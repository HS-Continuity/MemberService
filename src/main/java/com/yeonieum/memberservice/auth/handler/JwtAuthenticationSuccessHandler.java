package com.yeonieum.memberservice.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonieum.memberservice.auth.service.TokenService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.yeonieum.memberservice.auth.util.JwtUtils.ACCESS_TOKEN;
import static com.yeonieum.memberservice.auth.util.JwtUtils.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // Jwt 로그인(인증) 성공 시 호출 될 성공핸들러
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, String> tokenMap = jwtUtils.createTokenForLogin(customUserDetails.getCustomUserDto());
        // 토큰 영속화 추가
        //tokenService.saveRefreshToken(customUserDetails.getCustomUserDto().getUsername(), tokenMap.get(REFRESH_TOKEN), jwtUtils.getRemainingExpirationTime(tokenMap.get(ACCESS_TOKEN)));

        response.setHeader(HttpHeaders.AUTHORIZATION, tokenMap.get(ACCESS_TOKEN));
        response.setContentType("application/json");
        response = jwtUtils.addRefreshTokenToHttpOnlyCookie(response, tokenMap.get(REFRESH_TOKEN), customUserDetails.getCustomUserDto().getRole().getRoleType());
        response.setStatus(HttpStatus.OK.value());
        response.flushBuffer();
    }
}
