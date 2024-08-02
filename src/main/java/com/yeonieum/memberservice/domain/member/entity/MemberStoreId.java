package com.yeonieum.memberservice.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MemberStoreId implements Serializable {
    private Long customerId;
    private String memberId;
}

