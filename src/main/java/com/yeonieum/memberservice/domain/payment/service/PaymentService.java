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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<PaymentResponse.OfRetrieveMemberPaymentCard> retrieveMemberPaymentCards(String memberId, boolean isDefault) {

        if (!memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원 ID 입니다.");
        }

        List<MemberPaymentCard> memberPaymentCards = memberPaymentCardRepository.findByMember_MemberId(memberId);

        //리스트 스트림으로 변환
        Stream<MemberPaymentCard> cardStream = memberPaymentCards.stream();

        //필터 이용 -> IsDefaultPaymentCard값이 ACTIVE인 것만 추출
        if (isDefault) {
            cardStream = cardStream.filter(card -> card.getIsDefaultPaymentCard() == ActiveStatus.ACTIVE);
        }

        return cardStream
                .sorted(Comparator.comparing((MemberPaymentCard card) -> card.getIsDefaultPaymentCard() != ActiveStatus.ACTIVE))
                .map(PaymentResponse.OfRetrieveMemberPaymentCard::convertedBy)
                .collect(Collectors.toList()); //스트림을 리스트로 수집
    }

    /**
     * 회원의 결제카드 등록
     * @param registerMemberPaymentCardDto 등록할 결제카드의 정보
     * @throws IllegalArgumentException 존재하지 않는 회원 ID인 경우
     * @throws IllegalStateException 등록했는 결제카드가 5개를 초과할 경우
     * @throws IllegalStateException 이미 존재하는 은행 및 계좌번호 조합인 경우
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

        boolean cardExists = memberPaymentCardRepository.findByCardCompanyAndCardNumber(registerMemberPaymentCardDto.getCardCompany(), registerMemberPaymentCardDto.getCardNumber()).isPresent();
        if (cardExists) {
            throw new IllegalStateException("이미 존재하는 은행 및 계좌번호 조합입니다.");
        }

        // 새 카드가 기본 결제카드로 설정될 경우, 기존의 기본 결제카드 상태를 비활성화
        if (registerMemberPaymentCardDto.getIsDefaultPaymentCard()) {
            existingCards.stream()
                    .filter(card -> card.getIsDefaultPaymentCard() == ActiveStatus.ACTIVE)
                    .findFirst()
                    .ifPresent(card -> {
                        card.changeIsDefaultPaymentCard(ActiveStatus.INACTIVE);
                        memberPaymentCardRepository.save(card);
                    });
        }

        MemberPaymentCard memberPaymentCard = registerMemberPaymentCardDto.toEntity(targetMember);

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
    public boolean deleteMemberPaymentCard(Long memberPaymentCardId){
        if(memberPaymentCardRepository.existsById(memberPaymentCardId)) {
            memberPaymentCardRepository.deleteById(memberPaymentCardId);
            return true;
        } else {
            throw new IllegalArgumentException("존재하지 않는 결제카드 ID 입니다.");
        }
    }

    /**
     * 회원의 결제카드 대표카드로 설정
     * @param memberId 회원 ID
     * @param memberPaymentCardId 결제카드 ID
     * @throws IllegalArgumentException 존재하지 않는 결제카드 ID인 경우
     * @return 결제카드 삭제 성공 여부
     */
    @Transactional
    public boolean modifyMemberPaymentCard(String memberId, Long memberPaymentCardId){

        MemberPaymentCard targetMemberPaymentCard = memberPaymentCardRepository.findById(memberPaymentCardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 결제카드 ID 입니다."));

        List<MemberPaymentCard> existingCards = memberPaymentCardRepository.findByMember_MemberId(memberId);

        // 새 카드가 기본 결제카드로 설정될 경우, 기존의 기본 결제카드 상태를 비활성화
        existingCards.stream()
                .filter(card -> card.getIsDefaultPaymentCard() == ActiveStatus.ACTIVE)
                .findFirst()
                .ifPresent(card -> {
                    card.changeIsDefaultPaymentCard(ActiveStatus.INACTIVE);
                    memberPaymentCardRepository.save(card);
                });

        targetMemberPaymentCard.changeIsDefaultPaymentCard(ActiveStatus.ACTIVE);
        memberPaymentCardRepository.save(targetMemberPaymentCard);

        return true;
    }
}
