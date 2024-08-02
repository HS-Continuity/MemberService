package com.yeonieum.memberservice.domain.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsRequest {
    String phoneNumber;
    String userName;
}
