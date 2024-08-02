package com.yeonieum.memberservice.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoOAuthRevokeServiceImpl implements AuthRevokeService {

    private final RestTemplate restTemplate;
    @Override
    public String getRevokeURI(String accessToken) {
        return "https://kapi.kakao.com/v1/user/unlink";
    }

    @Override
    public void unlinkOAuth2(String accessToken) {
        try {
            URL url = new URL(getRevokeURI(accessToken));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer "+ accessToken);
            System.out.println(conn.getResponseMessage());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(String provider) {
        return "kakao".equals(provider);
    }
}
