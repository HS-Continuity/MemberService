package com.yeonieum.memberservice.domain.member.repository;

import com.yeonieum.memberservice.domain.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
}
