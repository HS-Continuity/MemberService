package com.yeonieum.memberservice.domain.address.entity;

import com.yeonieum.memberservice.domain.address.dto.AddressRequest;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.global.converter.ActiveStatusConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member_address")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long memberAddressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name ="recipient_name", nullable = false)
    private String recipientName;

    @Column(name ="recipient_phone_number", nullable = false)
    private String recipientPhoneNumber;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "general_address", nullable = false)
    private String generalAddress;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_default_address", nullable = false)
    @Builder.Default
    private ActiveStatus isDefaultAddress = ActiveStatus.INACTIVE;

    public void changeRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void changeRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public void changeAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void changeGeneralAddress(String generalAddress) {
        this.generalAddress = generalAddress;
    }

    public void changeDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void changeIsDefaultAddress(ActiveStatus isDefaultAddress) {
        this.isDefaultAddress = isDefaultAddress;
    }
}

