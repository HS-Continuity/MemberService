package com.yeonieum.memberservice.domain.member.service;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import com.yeonieum.memberservice.domain.member.exception.MemberException;
import com.yeonieum.memberservice.domain.member.repository.MemberCouponRepository;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.yeonieum.memberservice.domain.member.exception.MemberExceptionCode.MEMBER_COUPON_NOT_FOUND;
import static com.yeonieum.memberservice.domain.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원의 쿠폰 조회
     * @param memberId 회원 ID
     * @throws MemberException 존재하지 않는 회원 ID인 경우
     * @return 회원의 쿠폰 목록
     */
    @Transactional
    public List<MemberResponse.OfRetrieveMemberCoupon> retrieveMemberCoupons (String memberId){

        if(!memberRepository.existsById(memberId)){
            throw new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        LocalDate currentDate = LocalDate.now();

        //쿠폰의 유효기간이 지난 것은 조회 되지 않음
        List<MemberCoupon> memberCouponList = memberCouponRepository.findActiveCouponsByMemberId(memberId, currentDate);

        return memberCouponList.stream()
                .map(MemberResponse.OfRetrieveMemberCoupon::convertedBy)
                .collect(Collectors.toList());
    }

    /**
     * 회원의 쿠폰 사용 서비스
     * @param memberCouponId
     * @throws MemberException 존재하지 않는 회원 쿠폰 ID인 경우
     * @return
     */
    @Transactional
    public boolean useMemberCouponStatus(Long memberCouponId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new MemberException(MEMBER_COUPON_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (memberCoupon.getIsUsed().equals(ActiveStatus.ACTIVE)) {
            return false;
        }
        memberCoupon.changeUseStatus(ActiveStatus.ACTIVE);
        return true;
    }

    /**
     * 주문 실패 시 보상 트랜잭션
     * @param memberCouponId
     * @throws MemberException 존재하지 않는 회원 쿠폰 ID인 경우
     * @return
     */
    @Transactional
    public boolean unUseMemberCouponStatus(Long memberCouponId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new MemberException(MEMBER_COUPON_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (memberCoupon.getIsUsed().equals(ActiveStatus.INACTIVE)) {
            return false;
        }
        memberCoupon.changeUseStatus(ActiveStatus.INACTIVE);
        return true;
    }
}
