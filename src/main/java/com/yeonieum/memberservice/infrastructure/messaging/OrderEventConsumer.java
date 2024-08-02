package com.yeonieum.memberservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonieum.memberservice.domain.memberstore.service.MemberStoreService;
import com.yeonieum.memberservice.infrastructure.dto.OrderEventMessage;
import com.yeonieum.memberservice.infrastructure.dto.RegularDeliveryEventMessage;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ObjectMapper objectMapper;
    private final MemberStoreService memberStoreService;

    @KafkaListener(id = "order-notification-consumer-1", topics = "order-notification-topic", groupId = "order-notification-group-1", autoStartup = "true")
    public void listenOrderEventTopic(@Payload String message) {
        try {
            OrderEventMessage orderEventMessage = objectMapper.readValue(message, OrderEventMessage.class);
            if(orderEventMessage.getEventType().equals("PAYMENT_COMPLETED")) {
                String memberId = orderEventMessage.getMemberId();
                Long customerId = orderEventMessage.getCustomerId();

                memberStoreService.joinMemberStore(customerId, memberId);
            }
        } catch (JsonProcessingException e) {
            // 무시
        }
    }


    @KafkaListener(id = "regular-notification-consumer-1", topics = "regular-notification-topic", groupId = "regular-notification-group-1", autoStartup = "true")
    public void listenRegularOrderEventTopic(@Payload String message) {
        try {
            RegularDeliveryEventMessage regularDeliveryEventMessage = objectMapper.readValue(message, RegularDeliveryEventMessage.class);
            if(regularDeliveryEventMessage.getEventType().equals("APPLY")) {
                String memberId = regularDeliveryEventMessage.getMemberId();
                Long customerId = regularDeliveryEventMessage.getCustomerId();

                memberStoreService.joinMemberStore(customerId, memberId);
            }
        } catch (JsonProcessingException e) {
            // 무시
        }
    }
}
