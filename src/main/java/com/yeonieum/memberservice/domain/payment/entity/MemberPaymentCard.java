package com.yeonieum.memberservice.domain.payment.entity;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.converter.ActiveStatusConverter;
import com.yeonieum.memberservice.global.converter.YearMonthConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member_payment_card")
public class MemberPaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_payment_card_id")
    private Long memberPaymentCardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "card_company", nullable = false)
    private String cardCompany;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_password", nullable = false, length = 2)
    private String cardPassword;

    @Column(name = "cvc_number", nullable = false, length = 3)
    private String cvcNumber;

    //년월(ex. 0724) - 월 + 년
    @Convert(converter = YearMonthConverter.class)
    @Column(name = "card_expiration", nullable = false)
    private YearMonth cardExpiration;

    @Column(name = "master_birthday", nullable = false)
    private LocalDate masterBirthday;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_simple_payment_agreed", nullable = false)
    @Builder.Default
    private ActiveStatus isSimplePaymentAgreed = ActiveStatus.INACTIVE;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_default_payment_card", nullable = false)
    @Builder.Default
    private ActiveStatus isDefaultPaymentCard = ActiveStatus.INACTIVE;

}

