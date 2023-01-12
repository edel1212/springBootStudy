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
     * @Descripciton  :  해당 쿼리는 JPQL문법으로 쿼리와 다르다 DB로 돌려도 안나옴!
     * 
     *                  ✔ 얻고자 하는 데이터는 Board와 Member를 같이 받아오고 싶은것이다.
     *                    Board는 Memeber 와 연관관계를 맺고 있으므로 
     *                    Query 내에서 변수로 사용가능하다  ex) b.writer w
     *                    
     *                    🎈중요 1 ) b.writer w 와 같이 변수 형태로 사용한다
     *                           2 ) 연관 관계가 있을 경우 JOIN 시 ON 이 <b>필요없지만<b/>[현재의 경우임]
     *                               연관관계가 없을 경우 JOIN 시 ON 이 <b>필요하다<b/>
     *
     *
     *                  ✔ 해당 쿼리 결과를보면 Board 의 FK 인 Member writer 변수에 Lazy 처리 후
     *                     Member 데이터에 접근하했는데도 @Transactional을 사용하지 않았지만
     *                     Error가 발생하지 않는다!  
     *                    - ✅이유 : 아래 Query를 보면 Join을 이용하여 한번에 처리하여 데이터를
     *                               가져왔기 때문에 @Transactional을 사용하지 않아도 에러가
     *                               나지 않는것이다.
     * - Result Query
     *    Hibernate:
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
     * @Descripciton  : Reply는 @ManyToOne으로 Board와 연관관계를 맺고있지만
     *                  Board 쪽에서는 따로 Reply를 사용한 변수가 없으므로
     *                  Board를 기준으로 Query를 사용시 연관관계를 찾을 수 없기에 
     *                 "ON"을 사용하여 JOIN 조건을 추가해줘야한다. 
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
