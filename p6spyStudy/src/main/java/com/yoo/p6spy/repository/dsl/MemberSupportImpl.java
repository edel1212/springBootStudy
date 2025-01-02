package com.yoo.p6spy.repository.dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yoo.p6spy.entity.Member;
import com.yoo.p6spy.entity.QMember;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberSupportImpl implements MemberSupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Member> getAllList() {
        QMember qMember = QMember.member;
        return jpaQueryFactory.from(qMember)
                .select(qMember)
                .fetch();
    }
}
