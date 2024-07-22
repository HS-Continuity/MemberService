package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.sms.dto.SmsRequest;
import com.yeonieum.memberservice.domain.sms.service.SmsVarificationService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsVerificationController {
    DefaultMessageService defaultMessageService;
    private final SmsVarificationService smsVarificationService;

    @Value("${sms.apiKey}")
    private String apiKey;
    @Value("${sms.secretKey}")
    private String secretKey;
    @Value("${sms.apiUrl}")
    private String apiUrl;

    public SmsVerificationController(SmsVarificationService smsVarificationService) {
        this.smsVarificationService = smsVarificationService;
    }

    @PostConstruct
    public void init() {
        defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, apiUrl);
    }

    // 인증번호 발송 API
    @GetMapping("/verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SmsRequest smsRequest) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        defaultMessageService.send(smsVarificationService.writeVerificationCode(smsRequest));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyCode(@RequestParam String username, @RequestParam String code) {
        return new ResponseEntity<>(ApiResponse.builder()
                .result(smsVarificationService.verifyCode(username, code))
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
