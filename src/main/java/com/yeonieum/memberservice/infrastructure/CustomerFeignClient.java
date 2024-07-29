package com.yeonieum.memberservice.infrastructure;

import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.infrastructure.dto.RetrieveCustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "productservice", url = "http://localhost:8020")
public interface CustomerFeignClient {
    @GetMapping("/api/customer/auth/{businessNumber}")
    ResponseEntity<ApiResponse<RetrieveCustomerResponse>> retrieveCustomerForAuth(@PathVariable("businessNumber") String businessNumber);
}
