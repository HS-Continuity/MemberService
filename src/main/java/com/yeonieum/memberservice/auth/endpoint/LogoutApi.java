package com.yeonieum.memberservice.auth.endpoint;

import com.nimbusds.jose.proc.SecurityContext;
import com.yeonieum.memberservice.auth.service.TokenService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.global.auth.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.yeonieum.memberservice.auth.util.JwtUtils.BEARER_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LogoutApi {
    private final TokenService tokenService;
    private final JwtUtils jwtUtils;
    @Value("${cors.allowed.origin.yeonieum}")
    private String CORS_ALLOWED_ORIGIN_YEONIEUM;

    @Value("${cors.allowed.origin.dashboard}")
    private String CORS_ALLOWED_ORIGIN_DASHBOARD;
    @Role(role = {"ROLE_MEMBER", "ROLE_CUSTOMER"}, url = "/api/auth/logout", method = "POST")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(7))
                .orElse(null);

        tokenService.revokeAccessToken(accessToken, jwtUtils.getRemainingExpirationTime(accessToken));
        tokenService.deleteRefreshToken(name);
        String domain = jwtUtils.getRole(accessToken).equals("ROLE_MEMBER") ? CORS_ALLOWED_ORIGIN_YEONIEUM : CORS_ALLOWED_ORIGIN_DASHBOARD;
        response.setHeader("Set-Cookie", "REFRESH_TOKEN=; Path=/; Domain="+ domain +"; HttpOnly; Max-Age=0; SameSite=None; Secure;");

        return ResponseEntity.ok().build();
    }
}
