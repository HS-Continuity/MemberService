package com.yeonieum.memberservice.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverOAuthRevokeServiceImpl implements AuthRevokeService {
    private final RestTemplate restTemplate;
    @Override
    public String getRevokeURI(String accessToken) {
        return "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=LJpgW_VHiwxvLoOPPPP1&client_secret=XgfdEqtqSn&access_token="+accessToken;
    }

    @Override
    public void unlinkOAuth2(String accessToken) {
        String url = getRevokeURI(accessToken);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("예상치 못한 오류로 요청 처리에 실패");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(String provider) {
        return "naver".equals(provider);
    }
}
