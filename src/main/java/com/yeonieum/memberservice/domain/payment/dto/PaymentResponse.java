package com.yeonieum.memberservice.domain.payment.dto;

import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;

public class PaymentResponse {

    @Getter
    @Builder
    public static class OfRetrieveMemberPaymentCard {
        private Long memberPaymentCardId;
        private String cardCompany;
        private String cardNumber;
        private YearMonth cardExpiration;
        private ActiveStatus isDefaultPaymentCard;

        public static OfRetrieveMemberPaymentCard convertedBy(MemberPaymentCard memberPaymentCard) {
            return OfRetrieveMemberPaymentCard.builder()
                    .memberPaymentCardId(memberPaymentCard.getMemberPaymentCardId())
                    .cardCompany(memberPaymentCard.getCardCompany())
                    .cardNumber(memberPaymentCard.getCardNumber())
                    .cardExpiration(memberPaymentCard.getCardExpiration())
                    .isDefaultPaymentCard(memberPaymentCard.getIsDefaultPaymentCard())
                    .build();
        }
    }
}
