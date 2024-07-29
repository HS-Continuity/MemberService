package com.yeonieum.memberservice.auth.endpoint;

import com.yeonieum.memberservice.auth.service.TokenService;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetailsService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.yeonieum.memberservice.auth.util.JwtUtils.ACCESS_TOKEN;
import static com.yeonieum.memberservice.auth.util.JwtUtils.BEARER_PREFIX;

/**
 * gateway server에서 refresh token을 이용해 access token 재발급 요청을 처리하는 API
 */
@RestController
@RequiredArgsConstructor
public class ReIssueAccessTokenApi {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    private final TokenService tokenService;

    @GetMapping("/access-token")
    public ResponseEntity<?> reIssueAccessToken(@CookieValue(value = "REFRESH_TOKEN") String refreshToken,
                                                HttpServletRequest reqeust) {
        try{
            if(jwtUtils.validateToken(refreshToken)) {
                String role = jwtUtils.getRole(refreshToken);
                String accessToken = Optional.ofNullable(reqeust.getHeader(HttpHeaders.AUTHORIZATION))
                        .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                        .map(authHeader -> authHeader.substring(7))
                        .orElse(null);

                tokenService.revokeAccessToken(accessToken, jwtUtils.getRemainingExpirationTime(accessToken) < 0 ? 0 : jwtUtils.getRemainingExpirationTime(accessToken));
                CustomUserDetails customUserDetails = null;
                if(role.equals("ROLE_CUSTOMER")) {
                    //customUserDetails = (CustomUserDetails) userDetailsService.loadCustomerByUniqueId(jwtUtils.extractUsername(refreshToken));
                } else if (role.equals("ROLE_MEMBER")) {
                    customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(jwtUtils.extractUsername(refreshToken));

                }
                String newAccessToken = jwtUtils.createToken(ACCESS_TOKEN, customUserDetails.getCustomUserDto());

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.AUTHORIZATION, newAccessToken);
                return ResponseEntity.ok()
                        .headers(headers)
                        .build();
            }
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}
