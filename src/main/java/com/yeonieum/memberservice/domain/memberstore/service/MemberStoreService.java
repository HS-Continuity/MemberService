package com.yeonieum.memberservice.domain.memberstore.service;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.entity.MemberStoreId;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.domain.memberstore.dto.MemberStoreResponse;
import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.domain.memberstore.repository.MemberStoreRepository;
import com.yeonieum.memberservice.global.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberStoreService {

    private final MemberStoreRepository memberStoreRepository;
    private final MemberRepository memberRepository;

    /**
     * 고객의 회원 조회
     * @param customerId 고객 ID (업체)
     * @param pageable 페이징 정보
     * @return 고객 회원들의 정보
     */
    @Transactional
    public Page<MemberStoreResponse.OfRetrieveMemberInformation> retrieveStoreMembers(Long customerId, String memberId, String memberName, String memberEmail, String memberPhoneNumber, LocalDate memberBirthday, Gender gender, Pageable pageable) {
        Page<MemberStore> memberStores = memberStoreRepository.findByCriteria(customerId, memberId, memberName, memberEmail, memberPhoneNumber, memberBirthday, gender, pageable);
        return memberStores.map(MemberStoreResponse.OfRetrieveMemberInformation::convertedBy);
    }


    /**
     * 고객 상점에 회원 가입
     * @param customerId
     * @param memberId
     */
    @Transactional
    public void joinMemberStore(Long customerId, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );

        MemberStoreId memberStoreId = MemberStoreId.builder()
                        .memberId(memberId)
                        .customerId(customerId)
                        .build();

        if(memberStoreRepository.findByMemberStoreId(memberStoreId).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }

        memberStoreRepository.save(MemberStore.builder()
                .memberStoreId(memberStoreId)
                .member(member)
                .build()
        );
    }
}
