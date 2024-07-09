package com.yeonieum.memberservice.domain.member.repository;

import com.yeonieum.memberservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}

