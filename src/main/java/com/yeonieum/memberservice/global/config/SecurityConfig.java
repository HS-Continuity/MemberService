package com.yeonieum.memberservice.global.config;

import com.yeonieum.memberservice.auth.filter.JwtLoginFilter;
import com.yeonieum.memberservice.auth.handler.JwtAuthenticationFailureHandler;
import com.yeonieum.memberservice.auth.handler.JwtAuthenticationSuccessHandler;
import com.yeonieum.memberservice.auth.handler.OAuth2LogoutHandler;
import com.yeonieum.memberservice.auth.handler.OAuthAuthenticationSuccessHandler;
import com.yeonieum.memberservice.auth.oauth.CustomOAuth2UserServiceImpl;
import com.yeonieum.memberservice.auth.provider.JwtAuthenticationProvider;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserServiceImpl customOAuth2UserService;
    private final OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtUtils jwtUtils;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf((csrf) -> csrf.disable()); // restAPI csrf 프로텍션 비활성화(브라우저 요청이 아니므로)
        http.
                cors(Customizer.withDefaults());
        http.
                authorizeHttpRequests((auth) -> auth.requestMatchers(HttpMethod.OPTIONS,"*","/**").permitAll());

        http.
                httpBasic(AbstractHttpConfigurer::disable); // Basic 비활성화
        http.
                addFilterAt(new JwtLoginFilter(jwtAuthenticationSuccessHandler, jwtAuthenticationFailureHandler, getAuthenticationManger(), jwtUtils), UsernamePasswordAuthenticationFilter.class);
        http.
                sessionManagement((s) ->
                                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.
                authorizeHttpRequests((auth) -> auth.requestMatchers("/access-token","/api/auth/logout","/actuator/*","/api/member/join","/login", "/api/auth/login", "/oauth-login/logout", "/login/oauth2/code/*","/login-fail").permitAll()
                        .anyRequest().permitAll()); // 개발환경모드

        http.
                formLogin((form) -> form.loginPage("/login").loginProcessingUrl("/api/auth/login").permitAll()
                        .successHandler(jwtAuthenticationSuccessHandler)
                        .failureHandler(jwtAuthenticationFailureHandler));

        http.
                oauth2Login((auth) -> auth.loginPage("/loginpage").permitAll()
                        .successHandler(oAuthAuthenticationSuccessHandler)
                        .userInfoEndpoint((a) -> a.userService(customOAuth2UserService)));

//        http
//                .logout((auth) -> auth
//                        .addLogoutHandler(oAuth2LogoutHandler));


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("Content-Type", "application/json"));
        corsConfiguration.addExposedHeader("Bearer");
        corsConfiguration.addExposedHeader("provider");

        corsConfiguration.setAllowedOrigins(Arrays.asList("localhost:8010"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager getAuthenticationManger() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

}
