package com.yeonieum.memberservice.auth.handler;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(makeResponse(request, response, authentication));
    }

    public String makeResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails)((OAuth2AuthenticationToken)authentication).getPrincipal();

        String jwt = jwtUtils.createToken(userDetails.getCustomUserDto());
        jwtUtils.addJwtToHttpOnlyCookie(response, jwt, (String) request.getAttribute("token"));

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8010");
        return UriComponentsBuilder.fromUriString("/hello").toUriString();
    }
}
