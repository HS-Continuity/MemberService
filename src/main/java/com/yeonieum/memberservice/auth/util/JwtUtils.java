package com.yeonieum.memberservice.auth.util;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String PROVIDER_TOKEN = "PROVIDER_TOKEN";

    @Value("${cors.allowed.origin.yeonieum}")
    private String CORS_ALLOWED_ORIGIN_YEONIEUM;
    @Value("${cors.allowed.origin.dashboard}")
    private String CORS_ALLOWED_ORIGIN_DASHBOARD;

    @Value("${jwt.access-token-validation-time}")
    private long ACCESS_TOKEN_VALIDATION_TIME;
    @Value("${jwt.refresh-token-validation-time}")
    private long REFRESH_TOKEN_VALIDATION_TIME;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedBytes);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }


    public String createToken(String tokenType, CustomUserDto userDto) {
        long expirationTime = tokenType.equals(ACCESS_TOKEN) ? ACCESS_TOKEN_VALIDATION_TIME : REFRESH_TOKEN_VALIDATION_TIME;
        Date currentDateTime = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userDto.getUsername())
                        .claim("role", userDto.getRole())
                        .claim("username", userDto.getUsername())
                        .setExpiration(new Date(currentDateTime.getTime() + expirationTime))
                        .setIssuedAt(currentDateTime)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public Map<String, String> createTokenForLogin(CustomUserDto customUserDto) {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, createToken(ACCESS_TOKEN, customUserDto));
        tokenMap.put(REFRESH_TOKEN, "Bearer" + createToken(REFRESH_TOKEN, customUserDto).substring(7));

        return tokenMap;
    }

    public boolean validateToken(String token) {
        try{
            System.out.println("들어온;?");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT Signature");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claim is empty");
        }
        return false;
    }

    public HttpServletResponse addRefreshTokenToHttpOnlyCookie(HttpServletResponse response, String refreshToken, String role) {
        String domain = role.equals("ROLE_MEMBER") ? "www.yeonieum.com" : "admin.yeonieum.com";
        System.out.println(domain);
        response.setHeader("Set-Cookie", REFRESH_TOKEN +"=" + refreshToken + ";" +
                                                                "Path=/;" +
                                                                "Domain=" + "yeonieum.com" + ";" +
                                                                "HttpOnly; " +
                                                                "Max-Age=" + REFRESH_TOKEN_VALIDATION_TIME + ";" +
                                                                "SameSite=None;" +
                                                                "Secure;");
        return response;
    }

    public long getRemainingExpirationTime(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Date expirationDate = claims.getExpiration();
        long currentTimeMillis = System.currentTimeMillis();

        return expirationDate.getTime() - currentTimeMillis;
    }

    public String parsingToken(String token) {
        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7); // 베어러 접두사 제거 토큰 값 반환
        }

        else return token;
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }
}
