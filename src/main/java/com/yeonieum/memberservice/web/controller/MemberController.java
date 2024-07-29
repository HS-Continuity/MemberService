package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.dto.MemberRequest;
import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.service.MemberService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> registerMember(@RequestBody MemberRequest.RegisterMemberRequest request) {
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
    public ResponseEntity<ApiResponse> updateMember(@RequestParam String memberId, @RequestBody MemberRequest.UpdateMemberRequest request) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.updateMember(member, request);
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
}
