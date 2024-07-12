package com.yeonieum.memberservice.domain.memberstore.service;

import com.yeonieum.memberservice.domain.memberstore.dto.MemberStoreResponse;
import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.domain.memberstore.repository.MemberStoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStoreService {

    private final MemberStoreRepository memberStoreRepository;

    /**
     * 고객의 회원 조회
     * @param customerId 고객 ID (업체)
     * @param pageable 페이징 정보
     * @return 고객 회원들의 정보
     */
    @Transactional
    public Page<MemberStoreResponse.RetrieveMemberInformationDto> retrieveStoreMembers(Long customerId, Pageable pageable) {

        Page<MemberStore> memberStores = memberStoreRepository.findByCustomerIdAndInactiveMember(customerId, pageable);

        return memberStores.map(memberStore -> MemberStoreResponse.RetrieveMemberInformationDto.builder()
                .memberId(memberStore.getMember().getMemberId())
                .memberName(memberStore.getMember().getMemberName())
                .memberEmail(memberStore.getMember().getMemberEmail())
                .memberPhoneNumber(memberStore.getMember().getMemberPhoneNumber())
                .memberBirthday(memberStore.getMember().getMemberBirthday())
                .gender(memberStore.getMember().getGender())
                .build());
    }

}
