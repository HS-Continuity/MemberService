package com.yeonieum.memberservice.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // Jwt 로그인(인증) 성공 시 호출 될 성공핸들러
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.createToken(customUserDetails.getCustomUserDto());
        String json = new ObjectMapper().writeValueAsString(token);
        response.setContentType("application/json");
        response = jwtUtils.addJwtToHttpOnlyCookie(response, token,null);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
