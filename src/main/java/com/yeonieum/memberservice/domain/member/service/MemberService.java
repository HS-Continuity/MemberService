package com.yeonieum.memberservice.domain.member.service;

import com.yeonieum.memberservice.domain.member.dto.MemberRequest;
import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.repository.MemberRepository;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    private MemberResponse.RetrieveMemberDto convertToRetrieveMemberDto(Member member) {
//        return MemberResponse.RetrieveMemberDto.builder()
//                .memberId(member.getMemberId())
//                .memberName(member.getMemberName())
//                .memberEmail(member.getMemberEmail())
//                .memberPassword(member.getMemberPassword())
//                .memberBirthday(member.getMemberBirthday())
//                .memberPhoneNumber(member.getMemberPhoneNumber())
//                .gender(member.getGender())
//                .isDeleted(member.getIsDeleted())
//                .build();
//    }

    /**
     * 회원 가입 기능
     * @param request 회원가입 정보 DTO
     * @throws IllegalStateException 이미 존재하는 이메일일 경우
     * @throws IllegalStateException 이미 존재하는 아이디일 경우
     * @throws IllegalStateException 이미 존재하는 핸드폰 번호일 경우
     * @return 회원가입 성공여부
     */
    @Transactional
    public boolean registerMember(MemberRequest.RegisterMemberRequest request) {
        if (memberRepository.findByMemberEmail(request.getMemberEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByMemberId(request.getMemberId()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.findByMemberPhoneNumber(request.getMemberPhoneNumber()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 핸드폰 번호입니다.");
        }

        Member member = Member.builder()
                .memberId(request.getMemberId())
                .memberName(request.getMemberName())
                .memberEmail(request.getMemberEmail())
                .memberPassword(passwordEncoder.encode(request.getMemberPassword()))
                .memberBirthday(request.getMemberBirthday())
                .memberPhoneNumber(request.getMemberPhoneNumber())
                .gender(request.getGender())
                .build();

       memberRepository.save(member);

        return true;
    }

    /**
     * 회원 정보 수정
     * @param memberId 수정할 회원 ID
     * @param request 수정할 회원 정보 DTO
     * @throws IllegalStateException 회원을 찾을 수 없을 경우
     * @return 수정된 회원 정보
     */
    @Transactional
    public boolean updateMember(String memberId, MemberRequest.UpdateMemberRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다."));

        // 비밀번호 수정
        if(request.getMemberPassword() != null && !request.getMemberPassword().isEmpty()) {
            member.changeMemberPassword(passwordEncoder.encode(request.getMemberPassword()));
        }

        // 핸드폰 번호 수정
        if(request.getMemberPhoneNumber() != null) {
            member.changeMemberPhoneNumber(request.getMemberPhoneNumber());
        }

//        if(request.getMemberPassword() != null && !request.getMemberPassword().isEmpty()) {
//            member.changeMemberPassword(passwordEncoder.encode(request.getMemberPassword()));
//        }
//
//        if(request.getMemberName() != null) {
//            member.changeMemberName(request.getMemberName());
//        }
//
//        if(request.getMemberEmail() != null) {
//            member.changeMemberEmail(request.getMemberEmail());
//        }
//
//        if(request.getMemberBirthday() != null) {
//            member.changeMemberBirthday(request.getMemberBirthday());
//        }
//
//        if(request.getMemberPhoneNumber() != null) {
//            member.changeMemberPhoneNumber(request.getMemberPhoneNumber());
//        }
//
//        if(request.getGender() != null) {
//            member.changeGender(request.getGender());
//        }

        memberRepository.save(member);
        return true;
    }

}
