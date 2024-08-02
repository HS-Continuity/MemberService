package com.yeonieum.memberservice.domain.member.exception;

import com.yeonieum.memberservice.global.exceptions.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MemberException extends CustomException {

    public MemberException(MemberExceptionCode memberExceptionCode, HttpStatus status) {
        super(memberExceptionCode, status);
    }
}