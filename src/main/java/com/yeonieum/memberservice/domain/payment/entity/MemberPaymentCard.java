package com.yeonieum.memberservice.domain.payment.entity;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.converter.YearMonthConverter;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_password", nullable = false, length = 2)
    private String cardPassword;

    //년월(ex. 2407)
    @Convert(converter = YearMonthConverter.class)
    @Column(name = "card_expiration", nullable = false)
    private YearMonth cardExpiration;

    @Column(name = "master_birthday", nullable = false)
    private LocalDate masterBirthday;
}

