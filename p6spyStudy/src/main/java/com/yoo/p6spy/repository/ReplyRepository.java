package com.yoo.p6spy.repository;

import com.yoo.p6spy.entity.Board;
import com.yoo.p6spy.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
