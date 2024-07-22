package com.yeonieum.memberservice.domain.address.service;

import com.yeonieum.memberservice.domain.address.dto.AddressRequest;
import com.yeonieum.memberservice.domain.address.dto.AddressResponse;
import com.yeonieum.memberservice.domain.address.dto.AddressResponse.OfRetrieveMemberAddress;
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


    /**
     * 회원의 주소지 목록 조회
     * @param memberId 회원 ID
     * @param isDefault
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @return 회원의 주소지 목록
     */
    @Transactional
    public List<AddressResponse.OfRetrieveMemberAddress> retrieveMemberAddresses(String memberId, boolean isDefault) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID 입니다."));

        // 주소 목록 조회
        return memberAddressRepository.findByMember_MemberId(memberId).stream()
                .filter(address -> !isDefault || address.getIsDefaultAddress() == ActiveStatus.ACTIVE)  // ACTIVE인 데이터 추출
                .sorted(Comparator.comparing((MemberAddress address) -> address.getIsDefaultAddress() != ActiveStatus.ACTIVE))
                .map(AddressResponse.OfRetrieveMemberAddress::convertedBy)
                .collect(Collectors.toList());

    }

    /**
     * 회원의 주소지 등록
     * @param registerMemberAddress 등록할 주소지 정보
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @throws IllegalStateException 등록하는 주소지가 5개가 넘어가는 경우
     * @throws IllegalStateException 이미 존재하는 주소지인 경우
     * @return 주소지 등록 성공 여부
     */
    @Transactional
    public boolean registerMemberAddress(AddressRequest.OfRegisterMemberAddress registerMemberAddress) {
        Member targetMember = memberRepository.findById(registerMemberAddress.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID 입니다."));

        List<MemberAddress> existingAddresses = memberAddressRepository.findByMember_MemberId(registerMemberAddress.getMemberId());
        if (existingAddresses.size() > 5) {
            throw  new IllegalStateException("등록할 수 있는 주소지는 최대 5개 까지입니다.");
        }

        boolean addressExists = memberAddressRepository.findByGeneralAddressAndDetailAddress(
                registerMemberAddress.getGeneralAddress(), registerMemberAddress.getDetailAddress()).isPresent();
        if(addressExists) {
            throw new IllegalStateException("이미 존재하는 주소지입니다.");
        }

        // 대표 주소지 변경 (새 주소지를 대표 주소지로 설정)
        if (registerMemberAddress.getIsDefaultAddress()) {
            existingAddresses.stream()
                    .filter(address -> address.getIsDefaultAddress() == ActiveStatus.ACTIVE)
                    .findFirst()
                    .ifPresent(address -> {
                        address.changeIsDefaultAddress(ActiveStatus.INACTIVE);
                        memberAddressRepository.save(address);
                    });
        }

        MemberAddress memberAddress = registerMemberAddress.toEntity(targetMember);

        memberAddressRepository.save(memberAddress);
        return true;
    }

    /**
     * 회원의 주소지 삭제
     * @param memberAddressId 주소지 ID
     * @throws IllegalArgumentException 존재하지 않는 주소지 ID인 경우
     * @return 주소지 삭제 성공 여부
     */
    @Transactional
    public boolean deleteMemberAddress(Long memberAddressId) {
        if(memberAddressRepository.existsById(memberAddressId)) {
            memberAddressRepository.deleteById(memberAddressId);
            return true;
        } else {
            throw new IllegalArgumentException("존재하지 않는 주소지 ID 입니다.");
        }
    }

    /**
     * 회원의 주소지 수정
     * @param memberAddressId 주소지 ID
     * @param registerMemberAddress
     * @throws IllegalArgumentException 존재하지 않는 주소지 ID인 경우
     * @return 주소지 수정 성공 여부
     */
    @Transactional
    public boolean updateMemberAddress(Long memberAddressId, AddressRequest.OfRegisterMemberAddress registerMemberAddress) {
        MemberAddress targetMemberAddress = memberAddressRepository.findById(memberAddressId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주소지 ID 입니다."));

        // 수정 후의 주소 정보가 이미 다른 주소지로 등록되어 있는지 확인
        boolean addressExists = memberAddressRepository.findByGeneralAddressAndDetailAddress(
                registerMemberAddress.getGeneralAddress(), registerMemberAddress.getDetailAddress())
                .stream()
                .anyMatch(existingAddress -> !existingAddress.getMemberAddressId().equals(memberAddressId));

        if (addressExists) {
            throw new IllegalStateException("이미 존재하는 주소지입니다.");
        }

        // 수정할 주소지를 대표 주소지로 설정
        if (registerMemberAddress.getIsDefaultAddress()) {
            memberAddressRepository.findByMemberAndIsDefaultAddress(targetMemberAddress.getMember(), ActiveStatus.ACTIVE)
                    .ifPresent(address -> {
                        if (!address.getMemberAddressId().equals(memberAddressId)) {
                            address.changeIsDefaultAddress(ActiveStatus.INACTIVE);
                            memberAddressRepository.save(address);
                        }
                    });
        }

        targetMemberAddress.changeAddressName(registerMemberAddress.getAddressName());
        targetMemberAddress.changeRecipientName(registerMemberAddress.getRecipientName());
        targetMemberAddress.changeRecipientPhoneNumber(registerMemberAddress.getRecipientPhoneNumber());
        targetMemberAddress.changeGeneralAddress(registerMemberAddress.getGeneralAddress());
        targetMemberAddress.changeDetailAddress(registerMemberAddress.getDetailAddress());
        targetMemberAddress.changeIsDefaultAddress(registerMemberAddress.getIsDefaultAddress() ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE);

        memberAddressRepository.save(targetMemberAddress);


        return true;
    }

    /**
     * 회원의 주소지 대표 주소지로 설정
     * @param memberId 회원 ID
     * @param memberAddressId 주소지 ID
     * @throws IllegalArgumentException 존재하지 않는 주소지 ID인 경우
     * @return
     */
    @Transactional
    public boolean modifyMemberAddress(String memberId, Long memberAddressId) {
        MemberAddress targetMemberAddress = memberAddressRepository.findById(memberAddressId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 주소지 ID 입니다."));

        List<MemberAddress> existingAddresses = memberAddressRepository.findByMember_MemberId(memberId);

        // 새 주소지를 기본 주소지로 설정
        existingAddresses.stream()
                .filter(address -> address.getIsDefaultAddress() == ActiveStatus.ACTIVE)
                .findFirst()
                .ifPresent(address -> {
                    address.changeIsDefaultAddress(ActiveStatus.INACTIVE);
                    memberAddressRepository.save(address);
                });

        targetMemberAddress.changeIsDefaultAddress(ActiveStatus.ACTIVE);
        memberAddressRepository.save(targetMemberAddress);

        return true;
    }
}
