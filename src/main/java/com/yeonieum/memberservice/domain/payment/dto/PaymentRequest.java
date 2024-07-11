package com.yeonieum.memberservice.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class PaymentRequest {

    @Getter
    @NoArgsConstructor
    public static class RegisterMemberPaymentCardDto{

        private String memberId;
        private String cardCompany;
        private String cardNumber;
        private String cardPassword;
        private String cvcNumber;
        private String cardExpiration;
        private LocalDate masterBirthday;
        private boolean isSimplePaymentAgreed;
    }
}
