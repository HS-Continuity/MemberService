package com.yeonieum.memberservice.global.enums;

public enum Role {
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_GUEST("ROLE_GUEST"), // 사용자 추가 정보 입력하지 않은 OAuth 가입 사용자
    ROLE_CUSTOMER("ROLE_CUSTOMER");

    private String roleType;

    Role(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleType() {
        return roleType;
    }

    public static Role fromRoleType(String roleType) {
        switch (roleType) {
            case "ROLE_MEMBER":
                return ROLE_MEMBER;
            case "ROLE_ADMIN":
                return ROLE_ADMIN;
            case "ROLE_GUEST":
                return ROLE_GUEST;
            case "ROLE_CUSTOMER":
                return ROLE_CUSTOMER;
            default:
                throw new IllegalArgumentException("Invalid role type: " + roleType);
        }
    }
}
