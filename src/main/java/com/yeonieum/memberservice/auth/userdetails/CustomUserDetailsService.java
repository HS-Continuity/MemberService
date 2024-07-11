package com.yeonieum.memberservice.auth.userdetails;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username).orElseThrow(() ->new UsernameNotFoundException("Not Found UserId"));

        CustomUserDto customUserDto = new CustomUserDto();
        customUserDto.setPassword(member.getMemberPassword());
        customUserDto.setUsername(member.getMemberId());
        //System.out.println(member.getMemberId() + " " + member.getMemberPassword() + " " + customUserDto.getRole().get(0));

        return new CustomUserDetails(customUserDto);
    }
}
