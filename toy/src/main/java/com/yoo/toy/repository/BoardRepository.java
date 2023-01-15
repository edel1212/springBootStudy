package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import com.yoo.toy.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// SearchBoardRepository [ QuerydslSupport Interface 상속 추가 ]
public interface BoardRepository extends JpaRepository<Board,Long> , SearchBoardRepository {

    // b.writer를 사용 가능한것 Board 에서 해당 Member wrtier로 변수를 사용중이기 떄문이다.
    @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno =:bno")
    Object getBoardWithWriter(Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w" +
            " LEFT  JOIN Reply r ON r.board = b" +
            " GROUP BY b"
            ,countQuery = "SELECT COUNT(b) FROM Board b") //w 의 경우 b에 포함되어있기 때문에 없어도 괜찮다.
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

}
