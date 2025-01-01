package com.yoo.p6spy.repository;

import com.yoo.p6spy.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
