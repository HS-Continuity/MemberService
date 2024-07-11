package com.yeonieum.memberservice.domain.payment.service;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.domain.payment.dto.PaymentResponse;
import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.domain.payment.repository.MemberPaymentCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberPaymentCardRepository memberPaymentCardRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원의 결제카드 목록 조회
     * @param memberId 회원 ID
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @return 회원의 결제카드 목록
     */
    public List<PaymentResponse.RetrieveMemberPaymentCardDto> retrieveMemberPaymentCards(String memberId){

        if(!memberRepository.existsById(memberId)){
            throw new IllegalArgumentException("존재하지 않는 회원 ID 입니다.");
        }

        List<MemberPaymentCard> memberPaymentCards = memberPaymentCardRepository.findByMemberId(memberId);

        return memberPaymentCards.stream()
                .map(paymentCard -> PaymentResponse.RetrieveMemberPaymentCardDto.builder()
                        .memberPaymentCardId(paymentCard.getMemberPaymentCardId())
                        .cardCompany(paymentCard.getCardCompany())
                        .cardNumber(paymentCard.getCardNumber())
                        .build())
                .collect(Collectors.toList());
    }




}
