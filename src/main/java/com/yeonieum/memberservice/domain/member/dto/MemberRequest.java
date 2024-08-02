package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.global.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    @NoArgsConstructor
    public static class RegisterMemberRequest {

        @NotBlank(message = "아이디는 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 4~20자의 영문 대소문자와 숫자만 사용 가능합니다.")
        @Size(max = 20, message = "아이디는 최대 20자까지 가능합니다.")
        private String memberId;

        @NotBlank(message = "이름은 필수입니다.")
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2~10자의 한글만 사용 가능합니다.")
        @Size(max = 10, message = "이름은 최대 10자까지 가능합니다.")
        private String memberName;

        @NotBlank(message = "이메일은 필수입니다.")
        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
        private String memberEmail;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        @Size(min = 8, max = 100, message = "비밀번호는 최소 8자에서 최대 100자까지 가능합니다.")
        private String memberPassword;

        @NotNull(message = "생일은 필수입니다.")
        private LocalDate memberBirthday;

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String memberPhoneNumber;

        @NotNull(message = "성별은 필수입니다.")
        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMemberRequest {
        @NotBlank(message = "이름은 필수입니다.")
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2~10자의 한글만 사용 가능합니다.")
        @Size(max = 10, message = "이름은 최대 10자까지 가능합니다.")
        private String memberName;

        @NotBlank(message = "이메일은 필수입니다.")
        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
        private String memberEmail;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        private String memberPassword;

        @NotNull(message = "생일은 필수입니다.")
        private LocalDate memberBirthday;

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String memberPhoneNumber;

        @NotNull(message = "성별은 필수입니다.")
        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class VerifyPasswordRequest {

        @NotBlank(message = "회원 ID 입력은 필수입니다.")
        private String memberId;

        @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
        private String currentPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangePasswordRequest {

        @NotBlank(message = "회원 ID 입력은 필수입니다.")
        private String memberId;

        @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호 입력은 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        @Size(min = 8, max = 100, message = "비밀번호는 최소 8자에서 최대 100자까지 가능합니다.")
        private String newPassword;
    }
}