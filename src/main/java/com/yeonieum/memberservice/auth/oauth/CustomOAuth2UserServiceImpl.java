package com.yeonieum.memberservice.auth.oauth;

import com.yeonieum.memberservice.auth.userdetails.CustomUserDetails;
import com.yeonieum.memberservice.auth.userdetails.CustomUserDto;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 승인된 리다이렉트로 적어줬을때 -> 액세스토큰과 요청사용자 개인정보를 userRequest로 래핑하여 함께 가져와준다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                                                .getProviderDetails()
                                                .getUserInfoEndpoint()
                                                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);
        Member member = joinIfAbsent(oAuthAttributes);

        CustomUserDto customUserDto = CustomUserDto.builder()
                .username(member.getMemberId())
                .password(member.getMemberPassword())
                .build();

        request.setAttribute("provider", registrationId);
        request.setAttribute("token", userRequest.getAccessToken().getTokenValue());
        return CustomUserDetails.builder()
                .customUserDto(customUserDto)
                .attributes(oAuth2User.getAttributes())
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private Member joinIfAbsent(OAuthAttributes oAuthAttributes) {
        Optional<Member> member = memberRepository.findById(oAuthAttributes.getLoginId());
        Member createdMember;
        if(!member.isPresent()) {
            // 제공 동의한 개인정보를 통해 회원가입
            createdMember = Member.builder()
                    .memberId(oAuthAttributes.getLoginId())
                    .memberEmail(oAuthAttributes.getEmail())
                    .memberName(oAuthAttributes.getName())
                    .build();

            memberRepository.save(createdMember);
        } else {
            createdMember = member.get();
        }
        return createdMember;
    }
}
