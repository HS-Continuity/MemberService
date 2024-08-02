package com.yeonieum.memberservice.global.responses.code;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

@Getter
public enum SuccessCode {

    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    DELETE_SUCCESS(204, "204", "DELETE SUCCESS"),
    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),
    UPDATE_SUCCESS(200, "200", "UPDATE SUCCESS");

    private final int status;

    private final String code;

    private final String message;


    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @JsonCreator
    public static SuccessCode fromCode(String code) {
        for (SuccessCode successCode : SuccessCode.values()) {
            if (successCode.code.equals(code)) {
                return successCode;
            }
        }
        throw new IllegalArgumentException("Unknown enum code: " + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
