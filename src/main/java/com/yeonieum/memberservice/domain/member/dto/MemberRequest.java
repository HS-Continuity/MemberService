package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.global.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    @NoArgsConstructor
    public static class RegisterMemberRequest {
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 4~20자의 영문 대소문자와 숫자만 사용 가능합니다.")
        private String memberId;

        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2~10자의 한글만 사용 가능합니다.")
        private String memberName;

        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "올바른 이메일 형식이 아닙니다.")
        private String memberEmail;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        private String memberPassword;

        private LocalDate memberBirthday;

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String memberPhoneNumber;

        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMemberRequest {
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2~10자의 한글만 사용 가능합니다.")
        private String memberName;

        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "올바른 이메일 형식이 아닙니다.")
        private String memberEmail;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        private String memberPassword;

        private LocalDate memberBirthday;

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String memberPhoneNumber;

        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class VerifyPasswordRequest {
        @NotBlank(message = "회원 ID는 필수입니다.")
        private String memberId;

        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        private String currentPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank(message = "회원 ID는 필수입니다.")
        private String memberId;

        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        private String newPassword;
    }
}