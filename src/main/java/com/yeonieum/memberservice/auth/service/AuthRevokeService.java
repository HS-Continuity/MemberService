package com.yeonieum.memberservice.auth.service;

public interface AuthRevokeService {
    String getRevokeURI(String accessToken) ;
    void unlinkOAuth2(String accessToken);
    boolean supports(String provider);
}
