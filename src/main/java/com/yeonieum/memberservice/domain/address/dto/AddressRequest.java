package com.yeonieum.memberservice.domain.address.dto;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressRequest {

    @Getter
    @NoArgsConstructor
    public static class OfRegisterMemberAddress {

        private String memberId;

        @NotBlank(message = "배송지 이름은 필수입니다.")
        @Size(max = 50, message = "배송지 이름은 최대 30자까지 가능합니다.")
        private String addressName;

        @NotBlank(message = "수령인 이름은 필수입니다.")
        @Size(max = 10, message = "수령인 이름은 최대 10자까지 가능합니다.")
        private String recipientName;

        @NotBlank(message = "수령인 전화번호는 필수입니다.")
        @Size(max = 20, message = "수령인 전화번호는 최대 20자까지 가능합니다.")
        private String recipientPhoneNumber;

        @NotBlank(message = "일반 주소는 필수입니다.")
        @Size(max = 80, message = "일반 주소는 최대 80자까지 가능합니다.")
        private String generalAddress;

        @NotBlank(message = "상세 주소는 필수입니다.")
        @Size(max = 30, message = "상세 주소는 최대 30자까지 가능합니다.")
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
