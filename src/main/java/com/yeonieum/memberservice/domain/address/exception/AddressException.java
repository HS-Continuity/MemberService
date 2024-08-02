package com.yeonieum.memberservice.domain.address.exception;

import com.yeonieum.memberservice.global.exceptions.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AddressException extends CustomException {

    public AddressException(AddressExceptionCode addressExceptionCode, HttpStatus status) {
        super(addressExceptionCode, status);
    }
}
