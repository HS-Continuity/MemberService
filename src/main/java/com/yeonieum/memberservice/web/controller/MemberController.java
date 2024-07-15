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
