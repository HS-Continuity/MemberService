package com.yeonieum.memberservice.domain.payment.dto;

import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import lombok.Builder;
import lombok.Getter;

public class PaymentResponse {

    @Getter
    @Builder
    public static class OfRetrieveMemberPaymentCard {
        private Long memberPaymentCardId;
        private String cardCompany;
        private String cardNumber;
        private char isDefaultPaymentCard;

        public static OfRetrieveMemberPaymentCard convertedBy(MemberPaymentCard memberPaymentCard) {
            return OfRetrieveMemberPaymentCard.builder()
                    .memberPaymentCardId(memberPaymentCard.getMemberPaymentCardId())
                    .cardCompany(memberPaymentCard.getCardCompany())
                    .cardNumber(memberPaymentCard.getCardNumber())
                    .isDefaultPaymentCard(memberPaymentCard.getIsDefaultPaymentCard().getCode())
                    .build();
        }
    }
}
