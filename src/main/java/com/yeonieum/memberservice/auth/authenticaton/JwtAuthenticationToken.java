package com.yeonieum.memberservice.auth.authenticaton;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;
    private String role;

    //Before AuthenticationManager : 인증 전 인증객체
    public JwtAuthenticationToken(Object principal, Object credentials, String role) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.role = role;
        setAuthenticated(false);
    }

    //After AuthenticationManger : 인증 후 인증객체
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }


    @Override
    public void setAuthenticated(boolean authenticated) {
        if(authenticated) {
            throw new IllegalArgumentException("신뢰할 수 없는 토큰");
        }
        super.setAuthenticated(authenticated);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    public String getRole() {
        return this.role;
    }
}
