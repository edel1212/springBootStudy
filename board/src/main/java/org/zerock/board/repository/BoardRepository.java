package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
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
}
