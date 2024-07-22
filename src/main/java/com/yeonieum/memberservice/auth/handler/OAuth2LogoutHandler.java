package com.yeonieum.memberservice.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;



@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setHeader("Set-Cookie", HttpHeaders.AUTHORIZATION +"= ; " +
                "Path=/;" +
                "Domain=localhost; " +
                "HttpOnly; " +
                "Max-Age=0; " +
                "SameSite=None;" +
                "Secure;");
    }
}
