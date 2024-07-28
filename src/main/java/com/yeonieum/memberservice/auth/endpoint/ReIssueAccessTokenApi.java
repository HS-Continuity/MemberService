package com.yeonieum.memberservice.auth.endpoint;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetailsService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.yeonieum.memberservice.auth.util.JwtUtils.ACCESS_TOKEN;

/**
 * gateway server에서 refresh token을 이용해 access token 재발급 요청을 처리하는 API
 */
@RestController
@RequiredArgsConstructor
public class ReIssueAccessTokenApi {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/access-token")
    public ResponseEntity<?> reIssueAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        try{
            if(jwtUtils.validateToken(refreshToken)) {
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(jwtUtils.extractUsername(refreshToken));
                String accessToken = jwtUtils.createToken(ACCESS_TOKEN, customUserDetails.getCustomUserDto());

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.AUTHORIZATION, accessToken);
                return ResponseEntity.ok()
                        .headers(headers)
                        .build();
            }
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @Getter
    @NoArgsConstructor
    public static class RefreshTokenRequest {
        private String refreshToken;
    }
}
