package com.yeonieum.memberservice.domain.member.repository;

import com.yeonieum.memberservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberEmail(String memberEmail);
}

