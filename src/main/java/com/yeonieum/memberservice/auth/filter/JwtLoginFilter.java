package com.yeonieum.memberservice.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonieum.memberservice.auth.authenticaton.JwtAuthenticationToken;
import com.yeonieum.memberservice.auth.handler.JwtAuthenticationFailureHandler;
import com.yeonieum.memberservice.auth.handler.JwtAuthenticationSuccessHandler;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.stream.Collectors;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public JwtLoginFilter(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler, JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
        this.jwtAuthenticationFailureHandler = jwtAuthenticationFailureHandler;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.setFilterProcessesUrl("/api/auth/login");
        // loginprocessingurl과 동기화
    }

    @Override
    public JwtAuthenticationToken attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 인증 시도
        JwtAuthenticationToken authentication = null;
        ObjectMapper objectMapper = new ObjectMapper();

        if(request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            try {
                JsonMemberDto memberDto = objectMapper.readValue(
                        request.getReader().lines().collect(Collectors.joining()), JsonMemberDto.class);

                authentication =
                        new JwtAuthenticationToken(memberDto.getUsername(), memberDto.getPassword(), memberDto.getRoleType());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        //setDetails(request,authentication);
        return (JwtAuthenticationToken) authenticationManager.authenticate((JwtAuthenticationToken)authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        jwtAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, (JwtAuthenticationToken)authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }


    @Getter
    @Setter
    @NoArgsConstructor
    private static class JsonMemberDto {
        String username;
        String password;
        String roleType;
    }
}
