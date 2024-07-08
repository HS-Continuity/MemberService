package com.yeonieum.memberservice.global.responses;

import com.yeonieum.memberservice.global.responses.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private T result;

    private String resultCode;

    private String resultMsg;

    @Builder
    public ApiResponse(T result, SuccessCode successCode) {
        this.result = result;
        this.resultCode = successCode.getCode();
        this.resultMsg = successCode.getMessage();
    }
}
