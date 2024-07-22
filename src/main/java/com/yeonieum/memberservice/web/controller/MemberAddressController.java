package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.address.dto.AddressRequest;
import com.yeonieum.memberservice.domain.address.dto.AddressResponse.OfRetrieveMemberAddress;
import com.yeonieum.memberservice.domain.address.service.AddressService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member-address")
@RequiredArgsConstructor
public class MemberAddressController {

    private final AddressService addressService;

    @Operation(summary = "회원 주소지 목록 조회", description = "회원의 주소지 목록을 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 주소지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 주소지 조회 실패")
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> retrieveMemberAddress(
            @RequestParam("memberId") String memberId,
            @RequestParam(value = "isDefault", required = false, defaultValue = "false") boolean isDefault) {

        List<OfRetrieveMemberAddress> retrieveMemberAddresses = addressService.retrieveMemberAddresses(memberId, isDefault);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberAddresses)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "회원 주소지 등록", description = "회원 주소지를 등록하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 주소지 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 주소지 등록 실패")
    })
    @PostMapping("")
    public ResponseEntity<ApiResponse> registerMemberAddresses(@RequestBody AddressRequest.OfRegisterMemberAddress ofRegisterMemberAddress) {

        addressService.registerMemberAddress(ofRegisterMemberAddress);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 주소지 삭제", description = "회원 주소지를 삭제하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "회원 주소지 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "회원 주소지 삭제 실패")
    })
    @DeleteMapping("/{memberAddressId}")
    public ResponseEntity<ApiResponse> deleteMemberAddress(@PathVariable("memberAddressId") Long memberAddressId) {
        addressService.deleteMemberAddress(memberAddressId);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(null)
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build(), HttpStatus.OK);
    }


}






















