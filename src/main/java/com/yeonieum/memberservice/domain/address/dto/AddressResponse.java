package com.yeonieum.memberservice.domain.address.dto;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import lombok.Builder;
import lombok.Getter;

public class AddressResponse {

    @Getter
    @Builder
    public static class OfRetrieveMemberAddress {
        private Long memberAddressId;
        private String addressName;
        private String recipientName;
        private String recipientPhoneNumber;
        private String generalAddress;
        private String detailAddress;
        private ActiveStatus isDefaultAddress;

        public static OfRetrieveMemberAddress convertedBy(MemberAddress memberAddress) {
            return OfRetrieveMemberAddress.builder()
                    .memberAddressId(memberAddress.getMemberAddressId())
                    .addressName(memberAddress.getAddressName())
                    .recipientName(memberAddress.getRecipientName())
                    .recipientPhoneNumber(memberAddress.getRecipientPhoneNumber())
                    .generalAddress(memberAddress.getGeneralAddress())
                    .detailAddress(memberAddress.getDetailAddress())
                    .isDefaultAddress(memberAddress.getIsDefaultAddress())
                    .build();
        }
    }
}
