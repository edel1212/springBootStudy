package com.yoo.ex04.repository;

import com.yoo.ex04.entity.Board;
import com.yoo.ex04.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    //Board 삭제 시 Reply 삭제
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteByBno(Long bno);

    //Board를 가지고 Reply 목록 가져오기 - Query Method 사용
    List<Reply> getRepliesByBoardOrderByRno(Board board);


}
