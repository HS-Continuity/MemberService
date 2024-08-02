package com.yeonieum.memberservice.domain.member.repository;

import com.yeonieum.memberservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberId(String memberId);
    Set<Member> findAllByMemberIdIn(List<String> memberIds);
    Optional<Member> findByMemberPhoneNumber(String memberPhoneNumber);

}

