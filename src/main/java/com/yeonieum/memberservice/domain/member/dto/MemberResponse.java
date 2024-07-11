package com.yeonieum.memberservice.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberResponse {

    @Getter
    @Builder
    public static class RetrieveMemberCouponDto {

        private Long memberCouponId;
        private String memberId;
        private Long couponId;
        private String couponName;
        private int discountAmount;
        private LocalDate expirationDate;
    }
}
