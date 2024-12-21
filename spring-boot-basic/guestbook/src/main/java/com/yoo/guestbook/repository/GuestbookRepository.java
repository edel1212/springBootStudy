package com.yoo.guestbook.repository;

import com.yoo.guestbook.entitiy.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @Description : Querydsl 을 사용하기위해  QuerydslPredicateExecutor<EntityClass> 또한 상속받는다!
 * */
public interface GuestbookRepository extends JpaRepository<Guestbook,Long> , QuerydslPredicateExecutor<Guestbook> {
}
