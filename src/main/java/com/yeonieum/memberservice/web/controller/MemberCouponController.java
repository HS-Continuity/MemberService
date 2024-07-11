package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.service.MemberCouponService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/member-coupon")
@RequiredArgsConstructor
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    @Operation(summary = "회원 쿠폰 조회", description = "회원의 쿠폰목록을 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 쿠폰 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 쿠폰 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ApiResponse> retrieveMemberCoupons(@RequestParam("memberId") String memberId) {

        List<MemberResponse.RetrieveMemberCouponDto> retrieveMemberCoupons
                = memberCouponService.retrieveMemberCoupons(memberId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberCoupons)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
