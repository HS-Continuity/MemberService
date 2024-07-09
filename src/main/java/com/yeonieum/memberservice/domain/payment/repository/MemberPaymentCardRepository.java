package com.yeonieum.memberservice.domain.payment.repository;

import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPaymentCardRepository extends JpaRepository<MemberPaymentCard, Long> {
}
