package com.yeonieum.memberservice.domain.member.entity;

import com.yeonieum.memberservice.domain.coupon.entity.Coupon;
import com.yeonieum.memberservice.global.converter.ActiveStatusConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member_coupon")
public class MemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_id")
    private Long memberCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "use_date_time")
    private LocalDateTime useDateTime;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private ActiveStatus isUsed = ActiveStatus.INACTIVE;
}

