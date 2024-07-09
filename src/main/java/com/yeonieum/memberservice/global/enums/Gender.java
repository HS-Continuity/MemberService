package com.yeonieum.memberservice.global.enums;

public enum Gender {
    MALE('M'),
    FEMALE('F');

    private final char code;

    Gender(char code) {
        this.code = code;
    }

    public char getStoredGenderValue() {
        return code;
    }

    public static Gender fromCode(char code) {
        switch (code) {
            case 'M':
                return MALE;
            case 'F':
                return FEMALE;
            default:
                throw new IllegalArgumentException("Invalid gender code: " + code);
        }
    }
}

