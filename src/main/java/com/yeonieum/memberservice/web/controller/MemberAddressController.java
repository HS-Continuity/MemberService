package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.address.dto.AddressRequest;
import com.yeonieum.memberservice.domain.address.dto.AddressResponse;
import com.yeonieum.memberservice.domain.address.service.AddressService;
import com.yeonieum.memberservice.global.auth.Role;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member-address")
@RequiredArgsConstructor
public class MemberAddressController {

    private final AddressService addressService;

    @Operation(summary = "회원 배송지 목록 조회", description = "회원의 배송지 목록을 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 배송지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 배송지 조회 실패")
    })
    @Role(role = {"ROLE_MEMBER", "ROLE_CUSTOMER"}, url = "/api/member-address/list", method = "GET")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> retrieveMemberAddress(
            @RequestParam(value = "memberId") String memberId,
            @RequestParam(value = "isDefault", required = false, defaultValue = "false") boolean isDefault) {
        //String member = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AddressResponse.OfRetrieveMemberAddress> retrieveMemberAddresses = addressService.retrieveMemberAddresses(memberId, isDefault);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberAddresses)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "특정 id로 회원 배송지 조회", description = "특정 memberAddressId에 해당하는 회원의 배송지를 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 특정 배송지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 특정 배송지 조회 실패")
    })
    @Role(role = {"ROLE_MEMBER", "ROLE_CUSTOMER"}, url = "/api/member-address/{memberAddressId}", method = "GET")
    @GetMapping("/{memberAddressId}")
    public ResponseEntity<ApiResponse> getMemberAddress(@PathVariable("memberAddressId") Long memberAddressId) {
        AddressResponse.OfRetrieveMemberAddress memberAddress = addressService.getMemberAddressById(memberAddressId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(memberAddress)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }


    @Operation(summary = "회원 배송지 등록", description = "회원 배송지를 등록하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 배송지 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 배송지 등록 실패")
    })
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-address", method = "POST")
    @PostMapping
    public ResponseEntity<ApiResponse> registerMemberAddresses(@Valid @RequestBody AddressRequest.OfRegisterMemberAddress ofRegisterMemberAddress) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        addressService.registerMemberAddress(member, ofRegisterMemberAddress);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.CREATED);
    }


    @Operation(summary = "회원 배송지 삭제", description = "회원 배송지를 삭제하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "회원 배송지 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 배송지 삭제 실패")
    })
    @Role(role = {"ROLE_MEMBER", "ROLE_ADMIN"}, url = "/api/member-address/{memberAddressId}/delete", method = "DELETE")
    @DeleteMapping("/{memberAddressId}/delete")
    public ResponseEntity<ApiResponse> deleteMemberAddress(@PathVariable("memberAddressId") Long memberAddressId) {
        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        addressService.deleteMemberAddress(memberAddressId, member);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build(), HttpStatus.OK);
    }


    @Operation(summary = "회원 배송지 대표 배송지 수정", description = "회원의 배송지를 대표 배송지로 설정하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 배송지 대표 배송지로 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 배송지 대표 배송지로 수정 실패")
    })
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-address/{memberAddressId}", method = "PUT")
    @PutMapping("/{memberAddressId}")
    public ResponseEntity<ApiResponse> modifyMemberAddress(
            @PathVariable("memberAddressId") Long memberAddressId,
            @RequestParam("memberId") String memberId) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        addressService.modifyMemberAddress(member, memberAddressId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build(), HttpStatus.OK);
    }


    @Operation(summary = "회원 배송지 수정", description = "회원의 배송지 정보를 수정하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 배송지 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이미 존재하는 주소"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 주소지 ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Role(role = {"ROLE_MEMBER"}, url = "/api/member-address/{memberAddressId}/update", method = "PUT")
    @PutMapping("/{memberAddressId}/update")
    public ResponseEntity<ApiResponse> updateMemberAddress(
            @PathVariable("memberAddressId") Long memberAddressId,
            @Valid @RequestBody AddressRequest.OfRegisterMemberAddress registerMemberAddress) {

        String member = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isUpdated = addressService.updateMemberAddress(memberAddressId, member, registerMemberAddress);

        if (isUpdated) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .result(null)
                    .successCode(SuccessCode.UPDATE_SUCCESS)
                    .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ApiResponse.builder()
                    .result("배송지 수정에 실패했습니다.")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






















