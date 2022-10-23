package org.zerock.board.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;

/**
 * @Description : 해당 Class 는 BoardRepository 를 확장하기 위한 class 이며 확장 시 다음의 단계를 거친다
 * 
 *               ✔ 쿼리메서드나 @Query 등으로 처리 할수 없는 기능을 해당 기능들은 따로 interface -> Impl 로 구현
 *               ✔ 해당 상단의 조건에 맞는 구현체 interface + class 를 생성할때  <b>QuerydslRepositorySupport</b> 상속 필수
 *               ✔ 해당 구현 class  에서는 기능을 Q도메인 과 JPQLQuery 를 이용해서 구현함 
 *
 * */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    
    //QuerydslRepositorySupport 의 생성자를 요구함
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1.....");
        //querydsl 의 Q객체변수 생성
        QBoard board = QBoard.board;
        
        //JPQLQuery 객체 생성 매개변수로 사용할 Q객체변수 주입
        JPQLQuery<Board> jpqlQuery = from(board);
    
        //위에서 생상한 query객체에 원하는 Query문을 매서드형식으로 작성
        jpqlQuery.select(board).where(board.bno.eq(1L));

        log.info("-------------------------------------");
        //결과 주입
        List<Board> result = jpqlQuery.fetch();
        log.info("-------------------------------------");
        
        return null;
    }

    @Override
    public Board search2WithJoin() {

        log.info("Search2 with left Join");

        // QBoard 객체
        QBoard board = QBoard.board;
        // QReply  객체
        QReply reply = QReply.reply;
        
        //원하는 메인 데이터는 Board 이므로 제네릭 Board , from 도 QBoard 객체변수로
        JPQLQuery<Board> jpqlQuery = from(board);
        
        //left join 대상 (reply) -> on( join 조건 )
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        
        //결과 생성
        List<Board> result = jpqlQuery.fetch();

        return null;
    }

    @Override
    public Board search3WithJoin() {

        QBoard board = QBoard.board;

        QReply reply = QReply.reply;

        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        /**
         * 상단에서 테스트한 Join 들과 다른점은 groupBy 를 사용했다는 점 과
         * select 에서도 여러 객체를 가져오는 형태로 작성되었다는 것이다.
         *
         * 이렇게 정해진것은 엔티티객체 단위가 아니라 각각의 데이터를 가져오는 경우
         *  <b>Tuple</b> 이라는 객체를 이용한다
         *
         * */
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member,  reply.count());

        tuple.groupBy(board);

        log.info("-----------------------");
        log.info(tuple);
        log.info("-----------------------");

        List<Tuple> result = tuple.fetch();

        log.info("-----result-----");
        log.info(result);
        log.info("-----------------");


        return null;
    }

    /**
     * @Description : 해당 메서드의 Parameter 중에서 검색관련 Parameter 들을
     *                PageRequestDTO 를 사용하지 않는 이유는
     *                ✔ Repository 영역에서는 가능하면 DTO를 다루지 않는 편이 좋아서 이다!
     * */
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("search Page....");

        return null;
    }
}
