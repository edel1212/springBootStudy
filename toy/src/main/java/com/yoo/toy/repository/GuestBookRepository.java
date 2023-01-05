package com.yoo.toy.repository;

import com.yoo.toy.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> , QuerydslPredicateExecutor<GuestBook> {

}
