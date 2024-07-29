package com.yeonieum.memberservice.auth.provider;

import com.yeonieum.memberservice.auth.authenticaton.JwtAuthenticationToken;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String username = (String) authentication.getPrincipal();
        String password = (String) jwtAuthenticationToken.getCredentials();
        String role = (String) jwtAuthenticationToken.getRole();
        System.out.println(role);
        CustomUserDetails userDetails = null;
        if(role.equals("ROLE_CUSTOMER")) {
            userDetails = (CustomUserDetails) userDetailsService.loadCustomerByUniqueId(username);
        } else if(role.equals("ROLE_MEMBER")) {
            userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        } else {
            throw new BadCredentialsException("Invalid role");
        }


//        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid password");
//        }

        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new JwtAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
