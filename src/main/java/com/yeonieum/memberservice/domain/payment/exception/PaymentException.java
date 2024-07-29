package com.yeonieum.memberservice.domain.payment.exception;

import com.yeonieum.memberservice.global.exceptions.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PaymentException extends CustomException {

    public PaymentException(PaymentExceptionCode paymentExceptionCode, HttpStatus status) {
        super(paymentExceptionCode, status);
    }
}
