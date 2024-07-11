package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.payment.dto.PaymentRequest;
import com.yeonieum.memberservice.domain.payment.dto.PaymentResponse;
import com.yeonieum.memberservice.domain.payment.service.PaymentService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member-payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "회원 결제카드 목록 조회", description = "회원의 결제카드 목록을 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 결제카드 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 결제카드 조회 실패")
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> retrieveMemberPaymentCards(@RequestParam("memberId") String memberId) {

        List<PaymentResponse.RetrieveMemberPaymentCardDto> retrieveMemberPaymentCards
                = paymentService.retrieveMemberPaymentCards(memberId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberPaymentCards)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "회원 결제카드 등록", description = "회원의 결제카드를 등록하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 결제카드 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 결제카드 등록 실패")
    })
    @PostMapping("")
    public ResponseEntity<ApiResponse> registerMemberPaymentCards(@RequestBody PaymentRequest.RegisterMemberPaymentCardDto registerMemberPaymentCardDto) {

        paymentService.registerMemberPaymentCard(registerMemberPaymentCardDto);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 결제카드 삭제", description = "회원의 결제카드를 삭제하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "회원 결제카드 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 결제카드 삭제 실패")
    })
    @DeleteMapping("/{memberPaymentCardId}")
    public ResponseEntity<ApiResponse> deleteCartProduct(@PathVariable("memberPaymentCardId") Long memberPaymentCardId) {
        paymentService.deleteMemberPaymentCard(memberPaymentCardId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
