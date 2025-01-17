package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.payment.dto.PaymentRequest;
import com.yeonieum.memberservice.domain.payment.dto.PaymentResponse;
import com.yeonieum.memberservice.domain.payment.service.PaymentService;
import com.yeonieum.memberservice.global.auth.Role;
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
    @Role(role = {"ROLE_MEMBER", "ROLE_CUSTOMER"}, url = "/api/member-payment/list", method = "GET")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> retrieveMemberPaymentCards(
            @RequestParam("memberId") String memberId,
            @RequestParam(value = "isDefault", required = false, defaultValue = "false") boolean isDefault) {

        List<PaymentResponse.OfRetrieveMemberPaymentCard> retrieveMemberPaymentCards
                = paymentService.retrieveMemberPaymentCards(memberId, isDefault);

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
    @Role(role = {"ROLE_MEMBER", "ROLE_CUSTOMER"}, url = "/api/member-payment", method = "POST")
    @PostMapping
    public ResponseEntity<ApiResponse> registerMemberPaymentCards(@Valid @RequestBody PaymentRequest.OfRegisterMemberPaymentCard ofRegisterMemberPaymentCard) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        paymentService.registerMemberPaymentCard(member, ofRegisterMemberPaymentCard);

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
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-payment/{memberPaymentCardId}", method = "DELETE")
    @DeleteMapping("/{memberPaymentCardId}")
    public ResponseEntity<ApiResponse> deleteMemberPaymentCard(@PathVariable("memberPaymentCardId") Long memberPaymentCardId) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        paymentService.deleteMemberPaymentCard(memberPaymentCardId, member);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "회원 결제카드 대표카드 수정", description = "회원의 결제카드를 대표카드로 설정 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 결제카드 대표캬드로 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 결제카드 대표카드로 수정 실패")
    })
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-payment/{memberPaymentCardId}", method = "PUT")
    @PutMapping("/{memberPaymentCardId}")
    public ResponseEntity<ApiResponse> modifyMemberPaymentCard(
            @PathVariable("memberPaymentCardId") Long memberPaymentCardId,
            @RequestParam("memberId") String memberId) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        paymentService.modifyMemberPaymentCard(member, memberPaymentCardId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
