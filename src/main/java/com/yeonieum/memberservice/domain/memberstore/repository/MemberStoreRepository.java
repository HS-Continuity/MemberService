package com.yeonieum.memberservice.domain.memberstore.repository;

import com.yeonieum.memberservice.domain.member.entity.MemberStoreId;
import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberStoreRepository extends JpaRepository<MemberStore, MemberStoreId> {

    @Query("SELECT ms FROM MemberStore ms JOIN ms.member m WHERE ms.memberStoreId.customerId = :customerId AND m.isDeleted = com.yeonieum.memberservice.global.enums.ActiveStatus.INACTIVE")
    Page<MemberStore> findByCustomerIdAndInactiveMember(@Param("customerId") Long customerId, Pageable pageable);

}
