package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

//SearchBoardRepository 상속 추가
public interface BoardRepository extends JpaRepository<Board, Long> , SearchBoardRepository {
    /**
     * @Descripciton  :  해당 쿼리는 정상 적인 쿼리와 다르다 해당 쿼리를 DBMS로 돌려도 안나옴!
     * 
     *                  ✔ 해당 쿼리는 Board를 사용하고 있지만 Member를 같이 조회해야하는 상황이다
     *                    Entity Class 인 Board는 Memeber Class와 연관관계를 맺고 있으므로 
     *                    Query 내에서 변수로 사용가능하다  ex) b.writer w
     *                    
     *                    !중요 1) b.writer w 와 같이 변수 형태로 사용한다
     *                         2) 연관 관계가 있을 경우 JOIN 시 ON 이 <b>필요없지만<b/>
     *                             연관관계가 없을 경우 JOIN 시 ON 이 <b>필요하다<b/>
     *
     *
     *                  ✔ 해당 쿼리 결과를보면 Board 의 FK 인 Member writer 변수에 Lazy 처리를 했지만
     *                    join 이 사용된것을 확인 할 수 있다 따라서 해당 메서드를 구현할 떄는 @Transactional 이
     *                    없어도 에러가 안나는 것이다!
     *
     *     select
     *         board0_.bno as bno1_0_0_,
     *         member1_.email as email1_1_1_,
     *         board0_.moddate as moddate2_0_0_,
     *         board0_.regdate as regdate3_0_0_,
     *         board0_.content as content4_0_0_,
     *         board0_.title as title5_0_0_,
     *         board0_.writer_email as writer_e6_0_0_,
     *         member1_.moddate as moddate2_1_1_,
     *         member1_.regdate as regdate3_1_1_,
     *         member1_.name as name4_1_1_,
     *         member1_.password as password5_1_1_
     *     from
     *         board board0_
     *     left outer join
     *         member member1_
     *             on board0_.writer_email=member1_.email
     *     where
     *         board0_.bno=?
     *
     * **/
    @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno =:bno") //JPQL 처리
    Object getBoardWithWriter(@Param("bno") Long bno);

    /**
     * @Descripciton  : 해당 JPQL 의 JOIN Query를 보면 위와 다르게 ON 이용하여 연관관계를 만들어서
     *                  사용한 것을 확인 할 수 있다!
     * */
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);
    
    //페이징목록 + 댓글 수
    @Query("SELECT b, w, count(r) " +
            "FROM Board b " +
            "LEFT JOIN b.writer w " +
            "LEFT JOIN Reply r ON r.board = b " +
            "GROUP BY b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);



    ///////////////////////////////////////////////////////////////////////////////////////
    // 조회 화면에서 필요한 메서드 정의                                                       //
    ///////////////////////////////////////////////////////////////////////////////////////
    
    //단건 조회
    @Query("SELECT b, w, COUNT(r) " +
            "FROM Board b LEFT JOIN b.writer w " +
            "LEFT OUTER JOIN Reply r ON r.board = b " +
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

    //__Eof__
}
