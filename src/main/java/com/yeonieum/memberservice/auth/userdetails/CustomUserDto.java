package com.yeonieum.memberservice.auth.userdetails;

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
    //private String email;
    private String password;
    //private List<String> role = Arrays.asList("USER");
}
