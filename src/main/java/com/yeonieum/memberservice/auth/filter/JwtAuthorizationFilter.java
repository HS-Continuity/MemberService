package com.yeonieum.memberservice.auth.filter;
import com.yeonieum.memberservice.auth.authenticaton.JwtAuthenticationToken;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetailsService;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDto;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import com.yeonieum.memberservice.global.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private List<String> excludeUrlPatterns = new ArrayList<>(Arrays.asList(
            "/api/member",
            "/login-fail",
            "/login/oauth2/code/*",
            "/oauth-login/logout",
            "/loginpage",
            "/api/login",
            "/memberservice/api/auth/login",
            "/memberservice/api/member/join",
            "/memberservice/access-token",
            "/error",
            "/favicon.ico",
            "/*.png",
            "/*.gif",
            "/*.svg",
            "/*.jpg",
            "/*.html",
            "/*.css",
            "/*.js",
            "/static/**",
            "/templates/**",
            "/actuator/*",
            "/memberservice/api/permissions",
            "/memberservice/access-token",
            "/memberservice/api/auth/logout",
            "/memberservice/api/member/check-id",
            "/memberservice/api/member/summary",
            "/memberservice/api/member/summaries",
            ""
    ));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
    }

    // jwt토큰의 검증 filter class
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 존재하는 경우에만 검증 로직 시작
        // 3. OPTIONS 요청일 경우 => 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // [STEP1] Client에서 API를 요청할때 Header를 확인합니다.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("[+] header Check: " + header);

        try {
            // [STEP2-1] Header 내에 토큰이 존재하는 경우
            if (header != null && !header.equalsIgnoreCase("")) {

                // [STEP2] Header 내에 토큰을 추출합니다.
                String token = jwtUtils.parsingToken(header);
                // [STEP3] 추출한 토큰이 유효한지 여부를 체크합니다.
                if (jwtUtils.validateToken(token)) {
                    // [STEP4] 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
                    System.out.println("토큰 검증 성공");
                    CustomUserDetails customUserDetails = new CustomUserDetails(CustomUserDto.builder()
                            .role(Role.valueOf(jwtUtils.getRole(token)))
                            .username(jwtUtils.getUserName(token))
                            .acceessToken(token)
                            .password("")
                            .build());

                    JwtAuthenticationToken authentication =
                            new JwtAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);



                    filterChain.doFilter(request, response);
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.flushBuffer();
                }
            } // [STEP2-1] 토큰이 존재하지 않는 경우
            else {
                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.flushBuffer();
            }
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.flushBuffer();
        }
    }
}
