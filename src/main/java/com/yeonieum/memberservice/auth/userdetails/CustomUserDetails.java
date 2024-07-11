package com.yeonieum.memberservice.auth.userdetails;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    // spring bean 은 아님
    // 직접 주입해줘야하는 생성자 주입 멤버 필드
    private final CustomUserDto customUserDto;
    private Map<String, Object> attributes;
    private String nameAttributeKey;



    public CustomUserDetails(CustomUserDto customUserDto) {
        this.customUserDto = customUserDto;
    }

    public CustomUserDetails(CustomUserDto customUserDto,Map<String, Object> attributes, String nameAttributeKey) {
        this.customUserDto = customUserDto;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        //roles.add("ROLE_" + customUserDto.getRole().get(0));

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return customUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return (String) this.attributes.get(nameAttributeKey);

    }
}
