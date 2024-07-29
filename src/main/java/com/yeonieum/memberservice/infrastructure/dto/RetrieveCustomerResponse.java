package com.yeonieum.memberservice.infrastructure.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public  class RetrieveCustomerResponse {
    private String storeBusinessNumber;
    private Long customerId;
    private String password;
}