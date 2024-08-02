package com.yeonieum.memberservice.domain.payment.exception;

import com.yeonieum.memberservice.global.exceptions.code.CustomExceptionCode;

public enum PaymentExceptionCode implements CustomExceptionCode {

    PAYMENT_CARD_NOT_FOUND(9000, "존재하지 않는 결제카드 ID 입니다."),
    MAXIMUM_CARDS_EXCEEDED(9001, "등록할 수 있는 결제카드는 최대 5개까지 입니다."),
    CARD_ALREADY_EXISTS(9002, "이미 존재하는 은행 및 계좌번호 조합입니다.");

    private final int code;
    private final String message;

    PaymentExceptionCode(int code, String message) {
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
