package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.memberstore.dto.MemberStoreResponse;
import com.yeonieum.memberservice.domain.memberstore.service.MemberStoreService;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-store")
@RequiredArgsConstructor
public class MemberStoreController {

    private final MemberStoreService memberStoreService;

    @Operation(summary = "고객의 회원 목록 조회", description = "고객의 회원 목록을 조회하는 기능입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "고객 회원목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "고객 회원목록 조회 실패")
    })
    @GetMapping("/list/{customerId}")
    public ResponseEntity<ApiResponse> retrieveStoreMembers(@PathVariable("customerId") Long customerId,
                                                            @RequestParam(defaultValue = "0") int startPage,
                                                            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(startPage, pageSize);

        Page<MemberStoreResponse.RetrieveMemberInformationDto> retrieveMemberInformations
                = memberStoreService.retrieveStoreMembers(customerId, pageable);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberInformations)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }


}
