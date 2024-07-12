package com.yeonieum.memberservice.auth.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String loginId;


    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        switch (registrationId) {
            case "google" -> {
                return ofGoogle(registrationId, userNameAttributeName, attributes);
            }
            case "naver" -> {
                return ofNaver(registrationId, userNameAttributeName, attributes);
            }
            case "kakao" -> {
                return ofKakao(registrationId, userNameAttributeName, attributes);
            }
            default -> {
                return null;
            }
        }
    }

    private static OAuthAttributes ofGoogle(String regisrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .loginId(createId(regisrationId, (String) attributes.get("sub")))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String regisrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(attributes)
                .loginId(createId(regisrationId, (String) response.get("id")))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .loginId(createId(registrationId, (String) attributes.get("id")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static String createId(String registrationId, String providerId) {
        return registrationId + "_" + providerId;
    }

}
