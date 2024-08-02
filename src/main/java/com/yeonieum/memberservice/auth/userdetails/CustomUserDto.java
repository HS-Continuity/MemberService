package com.yeonieum.memberservice.auth.userdetails;

import com.yeonieum.memberservice.global.enums.Role;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserDto {
    private String acceessToken;
    private String username;
    private String password;
    private Role role;
}
