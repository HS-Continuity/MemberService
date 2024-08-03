package com.yeonieum.memberservice.infrastructure;

import com.yeonieum.memberservice.global.config.FeignConfig;
import com.yeonieum.memberservice.global.responses.ApiResponse;
import com.yeonieum.memberservice.infrastructure.dto.RetrieveCustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "productservice", url= "localhost:8020", configuration = FeignConfig.class)
public interface CustomerFeignClient {
    @GetMapping("/productservice/api/customer/auth/{businessNumber}")
    ResponseEntity<ApiResponse<RetrieveCustomerResponse>> retrieveCustomerForAuth(@PathVariable("businessNumber") String businessNumber);
}
