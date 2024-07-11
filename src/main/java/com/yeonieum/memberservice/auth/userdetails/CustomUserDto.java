package com.yeonieum.memberservice.auth.userdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDto {
    private String acceessToken;
    private String username;
    //private String email;
    private String password;
    //private List<String> role = Arrays.asList("USER");
}
