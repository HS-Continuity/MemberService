package com.yeonieum.memberservice.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

public class PaymentResponse {

    @Getter
    @Builder
    public static class RetrieveMemberPaymentCardDto {
        private Long memberPaymentCardId;
        private String cardCompany;
        private String cardNumber;
        private boolean isDefaultPaymentCard;
    }
}
