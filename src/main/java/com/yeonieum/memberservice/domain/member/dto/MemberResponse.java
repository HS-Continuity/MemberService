package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import com.yeonieum.memberservice.global.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @Builder
    public static class RetrieveMemberDto {
        private String memberId;
        private String memberName;
        private String memberEmail;
        private String memberPassword;
        private LocalDate memberBirthday;
        private String memberPhoneNumber;
        private Gender gender;
        private ActiveStatus isDeleted;

        public static MemberResponse.RetrieveMemberDto convertToRetrieveMemberDto(Member member) {
            return MemberResponse.RetrieveMemberDto.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .memberEmail(member.getMemberEmail())
                    .memberPassword(member.getMemberPassword())
                    .memberBirthday(member.getMemberBirthday())
                    .memberPhoneNumber(member.getMemberPhoneNumber())
                    .gender(member.getGender())
                    .isDeleted(member.getIsDeleted())
                    .build();
        }
    }
}
