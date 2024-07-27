package com.yeonieum.memberservice.auth.endpoint;

import com.yeonieum.memberservice.auth.service.TokenService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) {

        String accessToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(7))
                .orElse(null);

        tokenService.revokeAccessToken(accessToken, jwtUtils.getRemainingExpirationTime(accessToken));
        tokenService.deleteRefreshToken(authentication.getName());
        response.setHeader("Set-Cookie", "REFRESH_TOKEN=; Path=/; Domain=localhost; HttpOnly; Max-Age=0; SameSite=None; Secure;");

        return ResponseEntity.ok().build();
    }
}
