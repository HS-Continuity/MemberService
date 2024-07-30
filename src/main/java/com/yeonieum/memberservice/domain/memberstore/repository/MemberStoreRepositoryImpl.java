package com.yeonieum.memberservice.domain.memberstore.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeonieum.memberservice.domain.member.dto.MemberResponse;
import com.yeonieum.memberservice.domain.member.entity.Member;
import com.yeonieum.memberservice.domain.member.entity.QMember;
import com.yeonieum.memberservice.domain.memberstore.entity.MemberStore;
import com.yeonieum.memberservice.domain.memberstore.entity.QMemberStore;
import com.yeonieum.memberservice.global.enums.ActiveStatus;
import com.yeonieum.memberservice.global.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberStoreRepositoryImpl implements MemberStoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberStore> findByCriteria(Long customerId, String memberId, String memberName, String memberEmail, String memberPhoneNumber, LocalDate memberBirthday, Gender gender, Pageable pageable) {
        QMemberStore memberStore = QMemberStore.memberStore;
        QMember joinedMember = memberStore.member;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(memberStore.memberStoreId.customerId.eq(customerId))
                .and(joinedMember.isDeleted.eq(ActiveStatus.INACTIVE));


        if (memberId != null) {
            builder.and(joinedMember.memberId.like("%" + memberId + "%"));
        }
        if (memberName != null) {
            builder.and(joinedMember.memberName.like("%" + memberName + "%"));
        }
        if (memberEmail != null) {
            builder.and(joinedMember.memberEmail.like("%" + memberEmail + "%"));
        }
        if (memberPhoneNumber != null) {
            builder.and(joinedMember.memberPhoneNumber.like("%" + memberPhoneNumber + "%"));
        }
        if (memberBirthday != null) {
            builder.and(joinedMember.memberBirthday.eq(memberBirthday));
        }
        if (gender != null) {
            builder.and(joinedMember.gender.eq(gender));
        }

        List<MemberStore> results = queryFactory
                .selectFrom(memberStore)
                .join(joinedMember, member).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(memberStore)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<String> findMemberIdsByNamesAndPhoneNumber(String name, String phoneNumber) {
        QMember member = QMember.member;

        return queryFactory.select(member.memberId)
                .from(member)
                .where(
                        name != null ? member.memberName.contains(name) : null,
                        phoneNumber != null ? member.memberPhoneNumber.contains(phoneNumber) : null
                )
                .fetch();
    }

    @Override
    public List<MemberResponse.OrderMemberInfo> findMembersByNamesAndPhoneNumber(String name, String phoneNumber) {
        QMember member = QMember.member;

        return queryFactory.select(Projections.constructor(MemberResponse.OrderMemberInfo.class,
                member.memberId,
                member.memberName,
                member.memberPhoneNumber))
                .from(member)
                .where(
                        name != null ? member.memberName.contains(name) : null,
                        phoneNumber != null ? member.memberPhoneNumber.contains(phoneNumber) : null
                )
                .fetch();
    }
}
