package com.yeonieum.memberservice.domain.payment.repository;

import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPaymentCardRepository extends JpaRepository<MemberPaymentCard, Long> {

    List<MemberPaymentCard> findByMember_MemberId(String memberId);

    Optional<MemberPaymentCard> findByCardCompanyAndCardNumber(String cardCompany, String cardNumber);

    boolean existsByMemberPaymentCardIdAndMember_MemberId(Long memberPaymentCardId, String memberId);
}
