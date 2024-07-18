package com.yeonieum.memberservice.domain.address.service;

import com.yeonieum.memberservice.domain.address.dto.AddressResponse;
import com.yeonieum.memberservice.domain.address.dto.AddressResponse.RetrieveMemberAddress;
import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.domain.address.repository.MemberAddressRepository;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private MemberAddressRepository memberAddressRepository;
    private MemberRepository memberRepository;

    @Transactional
    public List<RetrieveMemberAddress> retrieveMemberAddresses(String memberId, boolean isDefault) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 주소 목록 조회
        return memberAddressRepository.findByMember_MemberId(memberId).stream()
                .filter(address -> !isDefault || address.getIsDefaultAddress() == ActiveStatus.ACTIVE)  // ACTIVE인 데이터 추출
                .sorted(Comparator.comparing((MemberAddress address) -> address.getIsDefaultAddress() != ActiveStatus.ACTIVE))
                .map(AddressResponse.RetrieveMemberAddress::convertedBy)
                .collect(Collectors.toList());

    }



}
