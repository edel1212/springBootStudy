package com.yoo.ex04.repository;

import com.yoo.ex04.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
