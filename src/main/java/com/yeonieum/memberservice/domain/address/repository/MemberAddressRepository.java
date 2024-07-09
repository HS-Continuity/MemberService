package com.yeonieum.memberservice.domain.address.repository;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
