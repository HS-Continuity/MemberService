package com.yeonieum.memberservice.auth.filter;

import com.yeonieum.memberservice.auth.authenticaton.JwtAuthenticationToken;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetailsService;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;

    private final JwtUtils jwtUtils;
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private List<String> excludeUrlPatterns = new ArrayList<>(Arrays.asList(
            "/api/member/join",
            "/login-fail",
            "/login/oauth2/code/*",
            "/oauth-login/logout",
            "/loginpage",
            "/api/login",
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
            "/actuator/*"
    ));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println(excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getRequestURI())));
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
                    Claims claims = jwtUtils.parseToken(token);
                    String username = claims.get("username", String.class);
                    String role = claims.get("role", String.class);

                    // [STEP5] 사용자 아이디가 존재하는지 여부 체크
                    if (username != null && !username.equalsIgnoreCase("")) {
                        UserDetails userDetails = null;
                        try {
                            userDetails = userDetailsService.loadUserByUsername(username);
                        } catch (UsernameNotFoundException e) {
                            e.printStackTrace();
                        }

                        JwtAuthenticationToken authentication =
                                new JwtAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        System.out.println("인증성공");
                        filterChain.doFilter(request, response);
                    } else {
                        //throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR); // "TOKEN isn't userId",
                        response.sendRedirect("/loginpage");
                        //filterChain.doFilter(request, response);
                        return;
                    }
                    // 토큰이 유효하지 않은 경우
                } else {
                    response.sendRedirect("/loginpage");
                    //throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR); // "TOKEN is invalid",
                }
            }
            // [STEP2-1] 토큰이 존재하지 않는 경우
            else {
                // restAPI와 SSR방식 나누어 처리
                response.sendRedirect("/loginpage");
                //filterChain.doFilter(request, response);
                //throw new BusinessExceptionHandler(ErrorCode.BUSINESS_EXCEPTION_ERROR); // "Token is null",
            }
        } catch (Exception e) {
            // Token 내에 Exception이 발생 하였을 경우 => 클라이언트에 응답값을 반환하고 종료합니다.
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            //JSONObject jsonObject = jsonResponseWrapper(e);
            //printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }
}
