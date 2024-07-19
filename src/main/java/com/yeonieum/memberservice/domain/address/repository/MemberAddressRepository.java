package com.yeonieum.memberservice.domain.address.repository;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    List<MemberAddress> findByMember_MemberId(String memberId);

    Optional<MemberAddress> findByGenenralAndDetailAddress(String generalAddress, String detailAddress);
}
