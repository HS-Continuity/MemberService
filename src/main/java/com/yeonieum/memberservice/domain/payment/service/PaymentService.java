package com.yeonieum.memberservice.domain.payment.service;

import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.domain.payment.dto.PaymentRequest;
import com.yeonieum.memberservice.domain.payment.dto.PaymentResponse;
import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.domain.payment.repository.MemberPaymentCardRepository;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    @Transactional
    public List<PaymentResponse.RetrieveMemberPaymentCardDto> retrieveMemberPaymentCards(String memberId){

        if(!memberRepository.existsById(memberId)){
            throw new IllegalArgumentException("존재하지 않는 회원 ID 입니다.");
        }

        List<MemberPaymentCard> memberPaymentCards = memberPaymentCardRepository.findByMember_MemberId(memberId);

        return memberPaymentCards.stream()
                .map(paymentCard -> PaymentResponse.RetrieveMemberPaymentCardDto.builder()
                        .memberPaymentCardId(paymentCard.getMemberPaymentCardId())
                        .cardCompany(paymentCard.getCardCompany())
                        .cardNumber(paymentCard.getCardNumber())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 회원의 결제카드 등록
     * @param registerMemberPaymentCardDto 등록할 결제카드의 정보
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @throws IllegalStateException 등록했는 결제카드가 5개를 초과할 경우
     * @return 결제카드 등록 성공 여부
     */
    @Transactional
    public boolean registerMemberPaymentCard(PaymentRequest.RegisterMemberPaymentCardDto registerMemberPaymentCardDto){

        Member targetMember = memberRepository.findById(registerMemberPaymentCardDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID 입니다."));

        List<MemberPaymentCard> existingCards = memberPaymentCardRepository.findByMember_MemberId(registerMemberPaymentCardDto.getMemberId());
        if (existingCards.size() >= 5) {
            throw new IllegalStateException("등록할 수 있는 결제카드는 최대 5개까지 입니다.");
        }

        MemberPaymentCard memberPaymentCard = MemberPaymentCard.builder()
                .member(targetMember)
                .cardCompany(registerMemberPaymentCardDto.getCardCompany())
                .cardNumber(registerMemberPaymentCardDto.getCardNumber())
                .cardPassword(registerMemberPaymentCardDto.getCardPassword())
                .cvcNumber(registerMemberPaymentCardDto.getCvcNumber())
                .cardExpiration(YearMonth.parse(registerMemberPaymentCardDto.getCardExpiration(), DateTimeFormatter.ofPattern("MMyy")))
                .masterBirthday(registerMemberPaymentCardDto.getMasterBirthday())
                .isSimplePaymentAgreed(registerMemberPaymentCardDto.isSimplePaymentAgreed() ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE)
                .build();

        memberPaymentCardRepository.save(memberPaymentCard);
        return true;
    }

    /**
     * 회원의 결제카드 삭제
     * @param memberPaymentCardId 결제카드 ID
     * @throws IllegalArgumentException 존재하지 않는 결제카드 ID인 경우
     * @return 결제카드 삭제 성공 여부
     */
    @Transactional
    public boolean deleteMemberPaymentCard(Long memberPaymentCardId) {
        if(memberPaymentCardRepository.existsById(memberPaymentCardId)) {
            memberPaymentCardRepository.deleteById(memberPaymentCardId);
            return true;
        } else {
            throw new IllegalArgumentException("존재하지 않는 결제카드 ID 입니다.");
        }
    }
}
