package com.yeonieum.memberservice.domain.sms.service;

import com.yeonieum.memberservice.domain.sms.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SmsVarificationService {

    @Value("${sms.fromPhoneNumber}")
    private String fromPhoneNumber;

    private final RedisTemplate<String, String> redisTemplate;

    public Message writeVerificationCode(SmsRequest smsRequest) {
        // 인증번호 발송 로직
        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(smsRequest.getPhoneNumber());
        int generateCode = generateCode();
        message.setText("[연이음] 인증번호 " + generateCode + "를 입력하세요.");

        redisTemplate.opsForValue().set(generateKey(smsRequest.getUserName()),
                String.valueOf(generateCode),
                150000L,
                TimeUnit.MILLISECONDS);

        return message;
    }

    public boolean verifyCode(String userName, String code) {
        String key = generateKey(userName);
        String verificationCode = redisTemplate.opsForValue().get(key);
        return code.equals(verificationCode);
    }


    public static int generateCode() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    private String generateKey(String userName) {
        return "verification:" + userName;
    }
}
