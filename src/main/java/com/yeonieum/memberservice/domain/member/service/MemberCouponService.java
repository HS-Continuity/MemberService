package com.yeonieum.memberservice.domain.member.service;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import com.yeonieum.memberservice.domain.member.repository.MemberCouponRepository;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원의 쿠폰 조회
     * @param memberId 회원 ID
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @return 회원의 쿠폰 목록
     */
    public List<MemberResponse.RetrieveMemberCouponDto> retrieveMemberCoupons (String memberId){

        if(!memberRepository.existsById(memberId)){
            throw new IllegalArgumentException("존재하지 않는 회원 ID 입니다.");
        }

        LocalDate currentDate = LocalDate.now();

        //쿠폰의 유효기간이 지난 것은 조회 되지 않음
        List<MemberCoupon> memberCouponList = memberCouponRepository.findActiveCouponsByMemberId(memberId, currentDate);

        return memberCouponList.stream()
                .map(mc -> MemberResponse.RetrieveMemberCouponDto.builder()
                        .memberCouponId(mc.getMemberCouponId())
                        .memberId(mc.getMember().getMemberId())
                        .couponId(mc.getCoupon().getCouponId())
                        .couponName(mc.getCoupon().getCouponName())
                        .discountAmount(mc.getCoupon().getDiscountAmount())
                        .expirationDate(mc.getExpirationDate())
                        .build())
                .collect(Collectors.toList());
    }
}
