package com.yeonieum.memberservice.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REVOKE = "revoke";

    public void saveRefreshToken(String memberId, String refreshToken, long timeoutInSeconds) {
        String key = "refresh_token:" + memberId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofSeconds(timeoutInSeconds));
    }


    public String getRefreshToken(String tokenId) {
        String key = "refresh_token:" + tokenId;
        return redisTemplate.opsForValue().get(key);
    }


    public void deleteRefreshToken(String memberId) {
        String key = generateRefreshTokenKey(memberId);
        redisTemplate.delete(key);
    }


    public void revokeAccessToken(String accessToken, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(accessToken, REVOKE, Duration.ofSeconds(timeoutInSeconds));
    }


    public String getAccessTokenStatus(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }


    public String generateRefreshTokenKey(String memberId) {
        return "refresh_token:" + memberId;
    }
}
