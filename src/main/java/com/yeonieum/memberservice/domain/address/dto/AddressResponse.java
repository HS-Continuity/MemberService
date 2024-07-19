package com.yeonieum.memberservice.domain.address.dto;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import lombok.Builder;
import lombok.Getter;

public class AddressResponse {

    @Getter
    @Builder
    public static class RetrieveMemberAddress {
        private Long memberAddressId;
        private String recipientName;
        private String recipientPhoneNumber;
        private String generalAddress;
        private String detailAddress;
        private ActiveStatus isDefaultAddress;

        public static RetrieveMemberAddress convertedBy(MemberAddress memberAddress) {
            return RetrieveMemberAddress.builder()
                    .memberAddressId(memberAddress.getMemberAddressId())
                    .recipientName(memberAddress.getRecipientName())
                    .recipientPhoneNumber(memberAddress.getRecipientPhoneNumber())
                    .generalAddress(memberAddress.getGeneralAddress())
                    .detailAddress(memberAddress.getGeneralAddress())
                    .isDefaultAddress(memberAddress.getIsDefaultAddress())
                    .build();
        }
    }
}
