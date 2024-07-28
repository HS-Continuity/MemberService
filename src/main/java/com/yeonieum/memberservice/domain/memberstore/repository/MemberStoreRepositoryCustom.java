package com.yeonieum.memberservice.domain.memberstore.repository;

import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.global.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MemberStoreRepositoryCustom {
    Page<MemberStore> findByCriteria(Long customerId, String memberId, String memberName, String memberEmail, String memberPhoneNumber, LocalDate memberBirthday, Gender gender, Pageable pageable);
}
