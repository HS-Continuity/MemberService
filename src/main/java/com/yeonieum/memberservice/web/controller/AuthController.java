package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.auth.service.AuthRevokeService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/rest")
@RequiredArgsConstructor
public class AuthController {
    private final List<AuthRevokeService> authRevokeServices;

    @GetMapping("/revoke/{provider}")
    public ResponseEntity<?> revoke(@PathVariable String provider, @CookieValue(value= "access_token", defaultValue ="")String accessToken) {
        if(accessToken == null || accessToken.equals("")){
            throw new IllegalArgumentException("토큰이 존재하지않습니다.");
        }
        AuthRevokeService authRevokeService = getSupportedAuthService(provider);
        authRevokeService.unlinkOAuth2(accessToken);

        return new ResponseEntity<>(ApiResponse.builder()
                        .result(null)
                        .successCode(SuccessCode.UPDATE_SUCCESS)
                        .build(), HttpStatus.OK);
    }
    public AuthRevokeService getSupportedAuthService(String provider){
        for(AuthRevokeService authRevokeService : authRevokeServices) {
            if(authRevokeService.supports(provider)) {
                return authRevokeService;
            }
        }
        return null;
    }
}
