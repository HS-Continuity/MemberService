package com.yeonieum.memberservice.domain.memberstore.dto;

import com.yeonieum.memberservice.global.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberStoreResponse {

    @Getter
    @Builder
    public static class RetrieveMemberInformationDto {
        private String memberId;
        private String memberName;
        private String memberEmail;
        private String memberPhoneNumber;
        private LocalDate memberBirthday;
        private Gender gender;
    }
}
