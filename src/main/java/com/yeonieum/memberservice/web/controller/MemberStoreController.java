package com.yeonieum.memberservice.web.controller;

import com.yeonieum.memberservice.domain.memberstore.dto.MemberStoreResponse;
import com.yeonieum.memberservice.domain.memberstore.service.MemberStoreService;
import com.yeonieum.memberservice.global.auth.Role;
import com.yeonieum.memberservice.global.enums.Gender;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    @Role(role = {"ROLE_CUSTOMER"}, url = "/api/member-store/list/{customerId}", method = "GET")
    @GetMapping("/list/{customerId}")
    public ResponseEntity<ApiResponse> retrieveStoreMembers(@PathVariable("customerId") Long customerId,
                                                            @RequestParam(required = false) String memberId,
                                                            @RequestParam(required = false) String memberName,
                                                            @RequestParam(required = false) String memberEmail,
                                                            @RequestParam(required = false) String memberPhoneNumber,
                                                            @RequestParam(required = false) LocalDate memberBirthday,
                                                            @RequestParam(required = false) Gender gender,
                                                            @RequestParam(defaultValue = "0") int startPage,
                                                            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(startPage, pageSize);
        Long customer = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("customer = " + customer);

        Page<MemberStoreResponse.OfRetrieveMemberInformation> retrieveMemberInformations
                = memberStoreService.retrieveStoreMembers(customerId, memberId, memberName, memberEmail, memberPhoneNumber, memberBirthday, gender, pageable);

        return new ResponseEntity<>(ApiResponse.builder()
                .result(retrieveMemberInformations)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build(), HttpStatus.OK);
    }
}
