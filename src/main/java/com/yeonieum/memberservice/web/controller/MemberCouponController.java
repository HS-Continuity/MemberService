package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.service.MemberCouponService;
import com.yeonieum.memberservice.global.auth.Role;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-coupon/list", method = "GET")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> retrieveMemberCoupons(@RequestParam("memberId") String memberId, @RequestParam("couponType") String couponType) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MemberResponse.OfRetrieveMemberCoupon> retrieveMemberCoupons
                = memberCouponService.retrieveMemberCoupons(member, couponType);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberCoupons)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "회원 쿠폰 사용", description = "회원의 쿠폰을 사용하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 쿠폰 사용 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 쿠헌 사용 실패")
    })
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-coupon/use-state", method = "PUT")
    @PutMapping("/use-status")
    public ResponseEntity<ApiResponse> useMemberCouponStatus(@RequestParam("memberCouponId") Long memberCouponId) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberCouponService.useMemberCouponStatus(memberCouponId, member))
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
