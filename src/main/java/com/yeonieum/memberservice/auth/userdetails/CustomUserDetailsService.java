package com.yeonieum.memberservice.auth.userdetails;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.global.enums.Role;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.infrastructure.CustomerFeignClient;
import com.yeonieum.memberservice.infrastructure.dto.RetrieveCustomerResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomerFeignClient customerFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username).orElseThrow(() ->new UsernameNotFoundException("Not Found UserId"));

        CustomUserDto customUserDto = new CustomUserDto();
        customUserDto.setPassword(member.getMemberPassword());
        customUserDto.setUsername(member.getMemberId());
        customUserDto.setRole(member.getRole());

        return new CustomUserDetails(customUserDto);
    }


    public UserDetails loadCustomerByUniqueId(String customerLoginId) {
        ResponseEntity<ApiResponse<RetrieveCustomerResponse>> response = null;
        try {
            response = customerFeignClient.retrieveCustomerForAuth(customerLoginId);
        } catch (FeignException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Not Found CustomerId");
        }
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new UsernameNotFoundException("Not Found CustomerId");
        }

        RetrieveCustomerResponse customer = response.getBody().getResult();

        CustomUserDto customUserDto = new CustomUserDto();
        customUserDto.setPassword(customer.getPassword());
        customUserDto.setUsername(String.valueOf(customer.getCustomerId()));
        customUserDto.setRole(Role.ROLE_CUSTOMER);

        return new CustomUserDetails(customUserDto);
    }

    public UserDetails loadCustomerById(Long customerId) {
        ResponseEntity<ApiResponse<RetrieveCustomerResponse>> response = null;
        try {
            response = customerFeignClient.retrieveCustomerForAuthId(customerId);
        } catch (FeignException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Not Found CustomerId");
        }
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new UsernameNotFoundException("Not Found CustomerId");
        }

        RetrieveCustomerResponse customer = response.getBody().getResult();

        CustomUserDto customUserDto = new CustomUserDto();
        customUserDto.setPassword(customer.getPassword());
        customUserDto.setUsername(String.valueOf(customer.getCustomerId()));
        customUserDto.setRole(Role.ROLE_CUSTOMER);

        return new CustomUserDetails(customUserDto);
    }
}
