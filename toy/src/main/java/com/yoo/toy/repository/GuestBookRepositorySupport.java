package com.yoo.toy.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yoo.toy.entity.GuestBook;
import com.yoo.toy.entity.QGuestBook;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description : 해당 Class 는 QueryFactory 테스트를 위함.
 * */
@Repository
public class GuestBookRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public GuestBookRepositorySupport(JPAQueryFactory queryFactory) {
        super(GuestBook.class);
        this.queryFactory = queryFactory;
    }

    public List<GuestBook> findByName(Long gunm) {
        QGuestBook qGuestBook = QGuestBook.guestBook;
        return queryFactory
                .selectFrom(qGuestBook)
                .where(qGuestBook.gnum.goe(gunm))
                .fetch();
    }

}
