package com.yeonieum.memberservice.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yeonieum.memberservice.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}

