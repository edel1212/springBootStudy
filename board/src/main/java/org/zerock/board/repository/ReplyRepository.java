package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Modifying //JPQL을 이용해서 Update, Delete를 실행하기 위해서는 해당 어노테이션이 필요함
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteByBno(Long bno);

    //게시물로 댓글 목록 가져오기 -- JpaRepository 내 메서드 쿼리 사용
    //✔ 굳이 Replies 가 아니어도 괜찮다 Reply 로 해도 문제 X
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
