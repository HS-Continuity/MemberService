package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberResponse {

    @Getter
    @Builder
    public static class OfRetrieveMemberCoupon {

        private Long memberCouponId;
        private String memberId;
        private Long couponId;
        private String couponName;
        private int discountAmount;
        private LocalDate expirationDate;

        public static OfRetrieveMemberCoupon convertedBy(MemberCoupon memberCoupon) {
            return OfRetrieveMemberCoupon.builder()
                    .memberCouponId(memberCoupon.getMemberCouponId())
                    .memberId(memberCoupon.getMember().getMemberId())
                    .couponId(memberCoupon.getCoupon().getCouponId())
                    .couponName(memberCoupon.getCoupon().getCouponName())
                    .discountAmount(memberCoupon.getCoupon().getDiscountAmount())
                    .expirationDate(memberCoupon.getCoupon().getExpirationDate())
                    .build();
        }
    }
}
