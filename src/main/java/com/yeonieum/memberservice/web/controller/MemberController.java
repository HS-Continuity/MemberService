package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.dto.MemberRequest;
import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.service.MemberService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원이 가입을 하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 가입 실패")
    })
    @PostMapping
    public ResponseEntity<ApiResponse> registerMember(@Valid @RequestBody MemberRequest.RegisterMemberRequest request) {
        memberService.registerMember(request);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.CREATED);
    }


    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 수정 실패")
    })
    @PutMapping
    public ResponseEntity<ApiResponse> updateMember(@Valid @RequestParam String memberId, @RequestBody MemberRequest.UpdateMemberRequest request) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.updateMember(memberId, request);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "아이디 중복 검사", description = "입력받은 아이디의 중복 여부를 검사합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "아이디 중복 검사 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse> checkDuplicateId(@RequestParam String memberId) {
        boolean isDuplicate = memberService.verifyMemberId(memberId);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(isDuplicate)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "현재 비밀번호 검증", description = "현재 비밀번호가 올바른지 검증하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 검증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "비밀번호 검증 실패")
    })
    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse> verifyPassword(@Valid @RequestBody MemberRequest.VerifyPasswordRequest request) {
        boolean isValid = memberService.verifyCurrentPassword(request.getMemberId(), request.getCurrentPassword());
        return new ResponseEntity<>(ApiResponse.builder()
                .result(isValid)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경", description = "회원의 비밀번호를 변경하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "현재 비밀번호가 일치하지 않음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "비밀번호 변경 실패")
    })
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody MemberRequest.ChangePasswordRequest request) {
        boolean success = memberService.changePassword(request.getMemberId(), request);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build(), HttpStatus.OK);
    }


    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping
    public ResponseEntity<ApiResponse> getMember(@RequestParam String memberId) {
        MemberResponse.RetrieveMemberDto member = memberService.getMember(memberId);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(member)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);

    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 처리를 하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 처리 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 탈퇴 처리 실패")
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteMember(@RequestParam String memberId) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.deleteMember(member);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build(), HttpStatus.OK);
    }


    @Operation(summary = "주문 서비스에 필요한 회원 정보 조회", description = "주문 서비스에 필요한 회원 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/order")
    public ResponseEntity<ApiResponse> getOrderMemberInfo(@RequestParam String memberId) {
        MemberResponse.OrderMemberInfo targetMember = memberService.getOrderMemberInfo(memberId);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(targetMember)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);

    }


    @Operation(summary = "주문 서비스에 필요한 회원 정보 벌크 조회", description = "주문 서비스에 필요한 회원 정보를 벌크 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/list/order")
    public ResponseEntity<ApiResponse> getOrderMemberInfo(@RequestParam List<String> memberIds) {
        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberService.getOrderMemberInfoMap(memberIds))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);

    }


    @Operation(summary = "회원의 요약 정보 조회", description = "회원의 요약 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getMemberSummary(@RequestParam String memberId) {
        MemberResponse.RetrieveSummary memberSummary = memberService.getMemberSummary(memberId);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberSummary)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);
    }

    @Operation(summary = "회원의 요약 정보 조회", description = "회원의 요약 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/summaries")
    public ResponseEntity<ApiResponse> getMemberSummaries(@RequestParam List<String> memberIds) {
        List<MemberResponse.OrderMemberInfo> memberSummaries = memberService.getMemberSummaries(memberIds);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberSummaries)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);
    }

    @Operation(summary = "회원들의 이름과 휴대전화로 필터링된 회원 ID 조회", description = "회원들의 이름과 휴대전화로 필터링된 회원 ID를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> getFilterMember(@RequestParam(required = false) String memberName,
                                                       @RequestParam(required = false) String memberPhoneNumber) {
        List<String> memberSummaries = memberService.getFilteredMemberIds(memberName, memberPhoneNumber);
        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberSummaries)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);
    }


    @Operation(summary = "회원들의 이름과 휴대전화로 필터링된 회원 정보 조회", description = "회원들의 이름과 휴대전화로 필터링된 회원 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/filter-map")
    public ResponseEntity<ApiResponse> getFilterMemberMap(@RequestParam(required = false) String memberName,
                                                       @RequestParam(required = false) String memberPhoneNumber) {

        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberService.getFilteredMemberInfoMap(memberName, memberPhoneNumber))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);
    }

    @Operation(summary = "통계자료에 필요한 회원정보 조회(연령대, 성별)", description = "통계자료에 필요한 회원 정보를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 정보 조회 실패")
    })
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getFilterMemberMap(@RequestParam String memberId) {

        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberService.getMemberStatistics(memberId))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(),HttpStatus.OK);
    }
}
