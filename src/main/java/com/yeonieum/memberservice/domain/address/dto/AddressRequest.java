package com.yeonieum.memberservice.domain.address.dto;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressRequest {

    @Getter
    @NoArgsConstructor
    public static class OfRegisterMemberAddress {

        private String memberAddressId;
        private String memberId;
        private String addressName;
        private String recipientName;
        private String recipientPhoneNumber;
        private String generalAddress;
        private String detailAddress;
        private Boolean isDefaultAddress;

        public MemberAddress toEntity(Member member) {
            return MemberAddress.builder()
                    .member(member)
                    .addressName(this.addressName)
                    .recipientName(this.recipientName)
                    .recipientPhoneNumber(this.recipientPhoneNumber)
                    .generalAddress(this.generalAddress)
                    .detailAddress(this.detailAddress)
                    .isDefaultAddress(this.isDefaultAddress ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE)
                    .build();
        }
    }
}
