package com.yeonieum.memberservice.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    @Value("${yeonieum.cors.domain}")
    private static String CORS_DOMAIN;
    private final long TOKEN_VALIDATION_TIME = 25 * 60 * 1000L; // 25분


    private String secretKey = "Ddfjladlewmndacafjlkcadfklfnqlekwnfklqjcxlzjalfd"; //4 배수 자리
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        // base64형식으로 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        // 디코딩된 비밀키를 hmacSha 방식으로 암호화
        key = Keys.hmacShaKeyFor(decodedBytes);
    }


    public String createToken(String username) { // role정보도 추가해야된다. UserDto를 아규먼트로 변경해야한다.
        Date currentDateTime = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)   // 사용자 식별값
                        //.claim(AUTHORIZATION_KEY, ) // role
                        //.claim("EMAIL", ) // email
                        .claim("username", username)
                        .setExpiration(new Date(currentDateTime.getTime() + TOKEN_VALIDATION_TIME)) // 유효기간
                        .setIssuedAt(currentDateTime)   // 발행시간
                        .signWith(key, signatureAlgorithm)  // 적용 암호화 알고리즘
                        .compact();
    }


    public HttpServletResponse addJwtToHttpOnlyCookieForSSR(String token, HttpServletResponse response, String accessToken) {
        response.setHeader("Set-Cookie",AUTHORIZATION_HEADER +"=" + token + "; " +
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
