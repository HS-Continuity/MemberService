package com.yeonieum.memberservice.domain.member.dto;

import com.yeonieum.memberservice.global.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class MemberRequest {
    @Getter
    @NoArgsConstructor
    public static class RegisterMemberRequest {
        private String memberId;
        private String memberName;
        private String memberEmail;
        private String memberPassword;
        private LocalDate memberBirthday;
        private String memberPhoneNumber;
        private Gender gender;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMemberRequest {
        private String memberName;
        private String memberEmail;
        private String memberPassword;
        private LocalDate memberBirthday;
        private String memberPhoneNumber;
        private Gender gender;
    }
}
