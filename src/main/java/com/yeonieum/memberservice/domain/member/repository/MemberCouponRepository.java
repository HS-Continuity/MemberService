package com.yeonieum.memberservice.domain.member.repository;

import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("SELECT mc FROM MemberCoupon mc " +
            "JOIN FETCH mc.coupon c " +
            "WHERE mc.member.memberId = :memberId " +
            "AND mc.expirationDate > :currentDate " +
            "AND mc.isUsed = com.yeonieum.memberservice.global.enums.ActiveStatus.INACTIVE")
    List<MemberCoupon> findActiveCouponsByMemberId(@Param("memberId") String memberId, @Param("currentDate") LocalDate currentDate);
}
