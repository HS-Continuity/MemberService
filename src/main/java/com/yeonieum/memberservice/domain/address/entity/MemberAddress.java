package com.yeonieum.memberservice.domain.address.entity;

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

    @Column(nullable = false)
    private String address;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_default_address", nullable = false)
    @Builder.Default
    private ActiveStatus isDefaultAddress = ActiveStatus.INACTIVE;
}

