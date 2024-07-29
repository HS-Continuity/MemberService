package com.yeonieum.memberservice.domain.address.repository;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    List<MemberAddress> findByMember_MemberId(String memberId);

    Optional<MemberAddress> findByGeneralAddressAndDetailAddress(String generalAddress, String detailAddress);

    Optional<MemberAddress> findByMemberAndIsDefaultAddress (Member member, ActiveStatus isDefaultAddress);

    boolean existsByMemberAddressIdAndMember_MemberId(Long memberAddressId, String memberId);

    Optional<MemberAddress> findByMemberAddressIdAndMember_MemberId(Long memberAddressId, String memberId);
}
