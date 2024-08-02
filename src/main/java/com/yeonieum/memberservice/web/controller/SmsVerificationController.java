package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.service.MemberService;
import com.yeonieum.memberservice.domain.sms.dto.SmsRequest;
import com.yeonieum.memberservice.domain.sms.service.SmsVarificationService;
import com.yeonieum.memberservice.global.auth.Role;
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
    private final MemberService memberService;

    @Value("${sms.apiKey}")
    private String apiKey;
    @Value("${sms.secretKey}")
    private String secretKey;
    @Value("${sms.apiUrl}")
    private String apiUrl;

    public SmsVerificationController(SmsVarificationService smsVarificationService, MemberService memberService) {
        this.smsVarificationService = smsVarificationService;
        this.memberService = memberService;
    }

    @PostConstruct
    public void init() {
        defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, apiUrl);
    }

    // 인증번호 발송 API
    @Role(role = {"*"}, url = "/api/sms/verification-code", method = "POST")
    @PostMapping("/verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SmsRequest smsRequest) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // 전화번호 중복 검사
        try {
            if (memberService.verifyPhoneNumber(smsRequest.getPhoneNumber())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            defaultMessageService.send(smsVarificationService.writeVerificationCode(smsRequest));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Role(role = {"*"}, url = "/api/sms/verify", method = "GET")
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyCode(@RequestParam String username, @RequestParam String code) {
        return new ResponseEntity<>(ApiResponse.builder()
                .result(smsVarificationService.verifyCode(username, code))
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
