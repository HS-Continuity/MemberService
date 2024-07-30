package com.yeonieum.memberservice.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderEventMessage {
    private String memberId;
    private Long customerId;
    private String orderDetailId;
    private String eventType;
    private String topic;
}
