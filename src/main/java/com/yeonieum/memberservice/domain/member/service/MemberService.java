package com.yeonieum.memberservice.domain.member.service;

import com.yeonieum.memberservice.domain.member.dto.MemberRequest;
import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.exception.MemberException;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.domain.memberstore.repository.MemberStoreRepositoryImpl;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import com.yeonieum.memberservice.global.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yeonieum.memberservice.domain.member.exception.MemberExceptionCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberStoreRepositoryImpl memberStoreRepositoryImpl;

    /**
     * 회원 가입 기능
     * @param request 회원가입 정보 DTO
     * @throws MemberException 이미 존재하는 이메일일 경우
     * @throws MemberException 이미 존재하는 아이디일 경우
     * @throws MemberException 이미 존재하는 핸드폰 번호일 경우
     * @return 회원가입 성공여부
     */
    @Transactional
    public boolean registerMember(MemberRequest.RegisterMemberRequest request) {
        if (memberRepository.findByMemberEmail(request.getMemberEmail()).isPresent()) {
            throw new MemberException(EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        if (memberRepository.findByMemberId(request.getMemberId()).isPresent()) {
            throw new MemberException(MEMBER_ID_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        if (memberRepository.findByMemberPhoneNumber(request.getMemberPhoneNumber()).isPresent()) {
            throw new MemberException(PHONE_NUMBER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        Member member = Member.builder()
                .memberId(request.getMemberId())
                .memberName(request.getMemberName())
                .memberEmail(request.getMemberEmail())
                .memberPassword(passwordEncoder.encode(request.getMemberPassword()))
                .memberBirthday(request.getMemberBirthday())
                .memberPhoneNumber(request.getMemberPhoneNumber())
                .gender(request.getGender())
                .role(Role.ROLE_MEMBER)
                .build();

       memberRepository.save(member);

        return true;
    }

    /**
     * 회원 정보 수정
     * @param memberId 수정할 회원 ID
     * @param request 수정할 회원 정보 DTO
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 수정된 회원 정보
     */
    @Transactional
    public boolean updateMember(String memberId, MemberRequest.UpdateMemberRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 수정
        if(request.getMemberPassword() != null && !request.getMemberPassword().isEmpty()) {
            member.changeMemberPassword(passwordEncoder.encode(request.getMemberPassword()));
        }

        // 핸드폰 번호 수정
        if(request.getMemberPhoneNumber() != null) {
            member.changeMemberPhoneNumber(request.getMemberPhoneNumber());
        }

        memberRepository.save(member);
        return true;
    }

    /**
     * 회원 정보 조회
     * @param memberId 조회할 회원 ID
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 조회된 회원 정보
     */
    public MemberResponse.RetrieveMemberDto getMember(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return MemberResponse.RetrieveMemberDto.convertToRetrieveMemberDto(member);
    }

    /**
     * 아이디 중복 검사
     * @param memberId 검사할 회원 ID
     * @return 중복 여부 (true: 중복, false: 중복 아님)
     */
    public boolean verifyMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).isPresent();
    }

    /**
     * 전화번호 중복 검사
     * @param phoneNumber 검사할 전화번호
     * @return 중복 여부 (true: 중복, false: 중복 아님)
     */
    public boolean verifyPhoneNumber(String phoneNumber) {
        return memberRepository.findByMemberPhoneNumber(phoneNumber).isPresent();
    }

    /**
     * 현재 비밀번호 검증
     * @param memberId 회원 ID
     * @param currentPassword 현재 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean verifyCurrentPassword(String memberId, String currentPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        System.out.println("currentPassword : " + currentPassword);
        return passwordEncoder.matches(currentPassword, member.getMemberPassword());
    }

    /**
     * 비밀번호 변경
     * @param memberId 회원 ID
     * @param request 비밀번호 변경 요청 DTO
     * @return 변경 성공 여부
     */
    @Transactional
    public boolean changePassword(String memberId, MemberRequest.ChangePasswordRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getMemberPassword())) {
            throw new MemberException(PASSWORD_NOT_MATCH, HttpStatus.NOT_FOUND);
        }

        member.changeMemberPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
        return true;
    }

    /**
     * 회원 탈퇴 (is_deleted를 "T"로 변경)
     * @param memberId 탈퇴할 회원의 ID
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 탈퇴 처리 성공 여부
     */
    @Transactional
    public boolean deleteMember(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        member.changeIsDeleted(ActiveStatus.ACTIVE);
        memberRepository.save(member);
        return true;
    }

    /**
     * 주문 서비스에 필요한 회원정보 조회
     * @param memberId 조회할 회원 ID
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 조회된 회원 정보
     */
    public MemberResponse.OrderMemberInfo getOrderMemberInfo(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return MemberResponse.OrderMemberInfo.convertedBy(member);
    }

    /**
     * 주문 서비스에 필요한 회원정보 벌크조회
     * @param memberIds 조회할 회원 ID 리스트
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 조회된 회원 정보
     */
    public Map<String, MemberResponse.OrderMemberInfo> getOrderMemberInfoMap(List<String> memberIds) {
        Set<Member> member = memberRepository.findAllByMemberIdIn(memberIds);

        return MemberResponse.OrderMemberInfo.convertedMapBy(member);
    }

    /**
     * 수정될 것) 출고 서비스에 필요한 회원정보 조회
     * @param memberId 조회할 회원 ID
     * @throws MemberException 회원을 찾을 수 없을 경우
     * @return 조회된 회원 정보
     */
    public MemberResponse.RetrieveSummary getMemberSummary(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return MemberResponse.RetrieveSummary.convertedBy(member);
    }

    /**
     * 출고 서비스에 필요한 회원정보 조회
     * @param memberIds 필터링 된 회원들의 ID
     * @return 조회된 회원 정보 (회원 ID, 회원 이름, 회원 휴대전화 번호)
     */
    @Transactional
    public List<MemberResponse.OrderMemberInfo> getMemberSummaries(List<String> memberIds) {
        List<Member> members = memberRepository.findAllById(memberIds);

        if (members.isEmpty()) {
            throw new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return members.stream()
                .map(MemberResponse.OrderMemberInfo::convertedBy)
                .collect(Collectors.toList());
    }

    /**
     * 회원 이름과 휴대전화로 필터링 된 회원들의 ID 반환 메서드
     * @param memberName 회원 이름
     * @param memberPhoneNumber 회원 전화번호
     * @return 필터링된 회원 ID들
     */
    @Transactional
    public List<String> getFilteredMemberIds(String memberName, String memberPhoneNumber) {
        return memberStoreRepositoryImpl.findMemberIdsByNamesAndPhoneNumber(memberName, memberPhoneNumber);
    }

    /**
     * 회원 이름과 휴대전화로 필터링 된 회원들의 MemberInfoMap 반환 메서드
     * @param memberName 회원 이름
     * @param memberPhoneNumber 회원 전화번호
     * @return 필터링된 회원 ID들
     */
    @Transactional
    public Map<String, MemberResponse.OrderMemberInfo> getFilteredMemberInfoMap(String memberName, String memberPhoneNumber) {
        List<MemberResponse.OrderMemberInfo> memberList =
                memberStoreRepositoryImpl.findMembersByNamesAndPhoneNumber(memberName, memberPhoneNumber);

        Map<String, MemberResponse.OrderMemberInfo> orderMemberInfoMap = new HashMap<>();
        for(MemberResponse.OrderMemberInfo memberInfo : memberList) {
            orderMemberInfoMap.put(memberInfo.getMemberId(), memberInfo);
        }
        return orderMemberInfoMap;
    }

    /**
     * 통계자료에 필요한 회원정보 조회 (연령대, 성별)
     * @param memberId 회원 ID
     * @return 회원 정보 (연령대, 성별)
     */
    @Transactional
    public MemberResponse.MemberStatistics getMemberStatistics(String memberId) {

        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        int ageRange;

        LocalDate memberBirthDay = targetMember.getMemberBirthday();

        LocalDate currentDate = LocalDate.now();
        if ((memberBirthDay != null) && (currentDate != null)) {
            int age = Period.between(memberBirthDay, currentDate).getYears();
            ageRange = (age / 10) * 10;
        } else {
            ageRange = 0;
        }

        MemberResponse.MemberStatistics memberStatistics = MemberResponse.MemberStatistics.convertedBy(ageRange, targetMember.getGender());

        return memberStatistics;
    }
}
