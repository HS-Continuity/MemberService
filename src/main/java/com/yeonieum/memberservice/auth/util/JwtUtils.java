package com.yeonieum.memberservice.auth.util;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    // 비밀 키
    // 유효 기간(ttl)
    // 토큰 생성
    // 토큰에서 사용자 이름 추출
    // 토큰에서 권한 정보 추출
    // 토큰 유효성 검증
    public static final String BEARER_PREFIX = "Bearer";

    @Value("${yeonieum.cors.domain}")
    private String CORS_DOMAIN = "localhost:8081";
    @Value("${jwt.token-validation-time}")
    private int TOKEN_VALIDATION_TIME; // 25분

    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;



    @PostConstruct
    public void init() {
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedBytes);
    }


    public String createToken(CustomUserDto userDto) {
        Date currentDateTime = new Date();
        System.out.println("userDto.getUsername() = " + userDto.getRole());
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userDto.getUsername())
                        .claim("role", userDto.getRole())
                        .claim("username", userDto.getUsername())
                        .setExpiration(new Date(currentDateTime.getTime() + TOKEN_VALIDATION_TIME))
                        .setIssuedAt(currentDateTime)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }


    public HttpServletResponse addJwtToHttpOnlyCookie(HttpServletResponse response, String token, String accessToken) {
        response.setHeader("Set-Cookie", HttpHeaders.AUTHORIZATION +"=" + token + "; " +
                                                                "Path=/;" +
                                                                "Domain=localhost;" +
                                                                "HttpOnly; " +
                                                                "Max-Age=604800; " +
                                                                "SameSite=None;" +
                                                                "Secure;");
        response.addHeader("Set-Cookie","access_token=" + accessToken + ";"+
                                                                        "Path=/;" +
                                                                        "Domain=localhost;" +
                                                                        "HttpOnly; " +
                                                                        "Max-Age=604800; " +
                                                                        "SameSite=None;" +
                                                                        "Secure;");
        return response;
    }// access token이 우리께 아니라 provider 액세스토큰. (구글 , 네이버, ,...)
}
