package com.yeonieum.memberservice.domain.payment.dto;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.global.converter.YearMonthConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class PaymentRequest {

    @Getter
    @NoArgsConstructor
    public static class OfRegisterMemberPaymentCard {

        private String memberId;

        @NotBlank(message = "카드회사는 필수입니다.")
        private String cardCompany;

        @NotBlank(message = "카드번호는 필수입니다.")
        @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$", message = "카드번호는 0000-0000-0000-0000 형식이어야 합니다.")
        @Size(min = 19, max = 19, message = "카드번호는 19자이어야 합니다.")  // "0000-0000-0000-0000" 형태로 19자
        private String cardNumber;

        @NotBlank(message = "카드 비밀번호는 필수입니다.")
        @Size(min = 2, max = 2, message = "카드 비밀번호(앞두자리)는 2자이어야 합니다.")
        private String cardPassword;

        @NotBlank(message = "cvc번호 입력은 필수입니다.")
        @Size(min = 3, max = 3, message = "카드 cvc 번호는 3자이어야 합니다.")
        private String cvcNumber;

        @NotBlank(message = "카드 유효기간 입력은 필수입니다.")
        private String cardExpiration;

        @NotNull(message = "카드 명의자의 생년월일은 필수입니다.")
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
