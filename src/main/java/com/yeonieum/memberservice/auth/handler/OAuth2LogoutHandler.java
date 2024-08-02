package com.yeonieum.memberservice.auth.handler;

import com.yeonieum.memberservice.auth.service.TokenService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.yeonieum.memberservice.auth.util.JwtUtils.BEARER_PREFIX;


@Component
@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {
    private final TokenService tokenService;
    private final JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(7))
                .orElse(null);

        tokenService.revokeAccessToken(accessToken, jwtUtils.getRemainingExpirationTime(accessToken));
        tokenService.deleteRefreshToken(authentication.getName());

        response.setHeader("Set-Cookie", HttpHeaders.AUTHORIZATION +"= ; " +
                "Path=/;" +
                "Domain=localhost; " +
                "HttpOnly; " +
                "Max-Age=0; " +
                "SameSite=None;" +
                "Secure;");
    }
}
