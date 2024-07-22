package com.yeonieum.memberservice.domain.member.entity;

import com.yeonieum.memberservice.domain.address.entity.MemberAddress;
import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.domain.payment.entity.MemberPaymentCard;
import com.yeonieum.memberservice.global.converter.ActiveStatusConverter;
import com.yeonieum.memberservice.global.converter.GenderConverter;
import com.yeonieum.memberservice.global.converter.RoleConverter;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import com.yeonieum.memberservice.global.enums.Gender;
import com.yeonieum.memberservice.global.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_email", nullable = false, unique = true)
    private String memberEmail;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @Column(name = "member_birthday", nullable = false)
    private LocalDate memberBirthday;

    @Column(name = "member_phone_number", nullable = false)
    private String memberPhoneNumber;

    @Convert(converter = GenderConverter.class)
    @Column(nullable = false)
    private Gender gender;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false)
    private Role role;

    @Convert(converter = ActiveStatusConverter.class)
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private ActiveStatus isDeleted = ActiveStatus.INACTIVE;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberAddress> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberPaymentCard> paymentCardList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberCoupon> memberCouponList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberStore> memberStoreList = new ArrayList<>();

    public void changeMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void changeMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public void changeMemberPassword(String newPassword) {
        this.memberPassword = newPassword;
    }

    public void changeMemberBirthday(LocalDate memberBirthday) {
        this.memberBirthday = memberBirthday;
    }

    public void changeMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }

    public void changeIsDeleted(ActiveStatus isDeleted) {
        this.isDeleted = isDeleted;
    }
}
