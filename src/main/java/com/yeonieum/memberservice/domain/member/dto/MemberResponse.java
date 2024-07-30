package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import com.yeonieum.memberservice.global.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.query.Order;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Getter
    @Builder
    public static class RetrieveSummary {
        private String memberName;
        private String memberPhoneNumber;

        public static RetrieveSummary convertedBy(Member member) {
            return RetrieveSummary.builder()
                    .memberName(member.getMemberName())
                    .memberPhoneNumber(member.getMemberPhoneNumber())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OrderMemberInfo {
        private String memberId;
        private String memberName;
        private String memberPhoneNumber;

        public OrderMemberInfo (String memberId, String memberName, String memberPhoneNumber) {
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberPhoneNumber = memberPhoneNumber;
        }


        public static OrderMemberInfo convertedBy(Member member) {
            return OrderMemberInfo.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .memberPhoneNumber(member.getMemberPhoneNumber())
                    .build();
        }

        public static Map<String, OrderMemberInfo> convertedMapBy(Set<Member> member) {
            return member.stream()
                    .collect(Collectors.toMap(Member::getMemberId, OrderMemberInfo::convertedBy));
        }
    }
}
