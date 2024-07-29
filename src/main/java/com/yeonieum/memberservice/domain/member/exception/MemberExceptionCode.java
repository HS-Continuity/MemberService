package com.yeonieum.memberservice.domain.member.exception;

import com.yeonieum.memberservice.global.exceptions.code.CustomExceptionCode;

public enum MemberExceptionCode implements CustomExceptionCode {

    MEMBER_NOT_FOUND(8000, "존재하지 않는 회원 ID 입니다"),
    MEMBER_COUPON_NOT_FOUND(8001, "존재하지 않는 회원 쿠폰 ID 입니다"),
    EMAIL_ALREADY_EXISTS(8002, "이미 존재하는 이메일입니다."),
    MEMBER_ID_ALREADY_EXISTS(8003, "이미 존재하는 아이디입니다."),
    PHONE_NUMBER_ALREADY_EXISTS(8004, "이미 존재하는 핸드폰 번호입니다."),
    PASSWORD_NOT_MATCH(8005, "현재 비밀번호가 일치하지 않습니다.");

    private final int code;
    private final String message;

    MemberExceptionCode(int code, String message) {
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
