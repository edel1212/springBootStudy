package com.yoo.toy.repository;

import com.yoo.toy.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Modifying //JPQL을 이용해서 Update, Delete를 실행하기 위해서는 해당 어노테이션이 필요함
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteByBno(Long bno);

}
