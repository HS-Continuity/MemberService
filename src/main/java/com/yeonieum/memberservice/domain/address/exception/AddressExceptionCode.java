package com.yeonieum.memberservice.domain.address.exception;

import com.yeonieum.memberservice.global.exceptions.code.CustomExceptionCode;

public enum AddressExceptionCode implements CustomExceptionCode {

    ADDRESS_NOT_FOUND(7000, "존재하지 않는 배송지 ID 입니다"),
    MAXIMUM_ADDRESSES_EXCEEDED(7001, "등록할 수 있는 주소지는 최대 5개 까지입니다."),
    ADDRESS_ALREADY_EXISTS(7002, "이미 존재하는 주소지입니다.");

    private final int code;
    private final String message;

    AddressExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}


