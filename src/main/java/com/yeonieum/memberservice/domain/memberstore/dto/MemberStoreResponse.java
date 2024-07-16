package com.yeonieum.memberservice.domain.memberstore.dto;

import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.global.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberStoreResponse {

    @Getter
    @Builder
    public static class OfRetrieveMemberInformation {
        private String memberId;
        private String memberName;
        private String memberEmail;
        private String memberPhoneNumber;
        private LocalDate memberBirthday;
        private Gender gender;

        public static OfRetrieveMemberInformation convertedBy(MemberStore memberStore) {
            return OfRetrieveMemberInformation.builder()
                    .memberId(memberStore.getMember().getMemberId())
                    .memberName(memberStore.getMember().getMemberName())
                    .memberEmail(memberStore.getMember().getMemberEmail())
                    .memberPhoneNumber(memberStore.getMember().getMemberPhoneNumber())
                    .memberBirthday(memberStore.getMember().getMemberBirthday())
                    .gender(memberStore.getMember().getGender())
                    .build();
        }
    }
}
