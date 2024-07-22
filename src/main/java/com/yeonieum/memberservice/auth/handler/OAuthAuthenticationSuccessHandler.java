package com.yeonieum.memberservice.auth.handler;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.yeonieum.memberservice.auth.util.JwtUtils.ACCESS_TOKEN;
import static com.yeonieum.memberservice.auth.util.JwtUtils.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        makeResponse(request, response, authentication);
        response.flushBuffer();
    }

    public void makeResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails)((OAuth2AuthenticationToken)authentication).getPrincipal();

        Map<String, String> tokenMap = jwtUtils.createTokenForLogin(userDetails.getCustomUserDto());
        jwtUtils.addRefreshTokenToHttpOnlyCookie(response, tokenMap.get(REFRESH_TOKEN));
        response.setHeader(HttpHeaders.AUTHORIZATION, tokenMap.get(ACCESS_TOKEN));

        // 영속화 추가
        request.getAttribute("token"); // providertoken
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8010");
    }
}
