package com.yeonieum.memberservice.domain.payment.dto;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.global.converter.YearMonthConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class PaymentRequest {

    @Getter
    @NoArgsConstructor
    public static class OfRegisterMemberPaymentCard {

        private String memberId;
        private String cardCompany;
        private String cardNumber;
        private String cardPassword;
        private String cvcNumber;
        private String cardExpiration;
        private LocalDate masterBirthday;
        private Boolean isSimplePaymentAgreed;
        private Boolean isDefaultPaymentCard;

        public MemberPaymentCard toEntity(Member member) {
            YearMonthConverter converter = new YearMonthConverter();
            return MemberPaymentCard.builder()
                    .member(member)
                    .cardCompany(this.cardCompany)
                    .cardNumber(this.cardNumber)
                    .cardPassword(this.cardPassword)
                    .cvcNumber(this.cvcNumber)
                    .cardExpiration(converter.convertToEntityAttribute(this.cardExpiration))
                    .masterBirthday(this.masterBirthday)
                    .isSimplePaymentAgreed(this.isSimplePaymentAgreed ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE)
                    .isDefaultPaymentCard(this.isDefaultPaymentCard ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE)
                    .build();
        }
    }
}
