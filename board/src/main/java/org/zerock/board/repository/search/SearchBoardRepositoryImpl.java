package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        /*
        * SELECT b, w, count(r) FROM Board b
        * LEFT JOIN b.writer w LEFT JOIN REPLY r ON r.board = b
        * */
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member,reply.count());

        //Querydsl
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);// bno 가 0보다 크고

        if(!type.isEmpty()){
            String[] typeArr = type.split("");
            //검색조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for(String typeData : typeArr){
                switch (typeData){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
                booleanBuilder.and(conditionBuilder);
            }
        }

        tuple.where(booleanBuilder); // 완성된 조건문을 tuple 에 주입

        /////////////////
        // Sort Logic  //
        /////////////////
        /**
         * @Description  : 정렬 시 문제
         *                 해당 Method 의 파라미터로 pageable 을 받아 JPQLQuery의 orderBy()로
         *                 처리하면 될것 같지만 JPQL 에서는 Pageable 의 Sort 객체를 지원하지
         *                 않는 다는 문제가 있다.
         *
         *                 ✔ 따라서 orderBy()의 경우 OderSpecifier<T extends Comparable>로 처리해야함
         *                 ✔ 해당 OderSpecifier 에서 매개변수의 타입은 2가지인데
         *                    - Order, Expression 인데 둘다 타입은 querydsl.core... 쪽이다.
         *
         *                 ❔ 그럼 왜 pageable 을 파라미터로 받았는가?
         *                    - sort 객체의 값을 forEach()를 사용해서 OderSpecifier 를 만들기 위해.
         *                    - JPQLQuery 의 offset() 과 limit()를 이용하여 페이지를 처리하기 위함.
         *
         *
         *
         *
         * **/

        // get sortObject from pageable
        Sort sort = pageable.getSort();

    //TODO 해당코드 미완성.. 확인필요 이해하기 어려움 .. 구글링 필요함...
        sort.forEach(order->{
            Order direction = order.isAscending() ? Order.ASC : Order.DESC; //어떤 정렬인지 삼항연산을 통해 주입
            String prop = order.getProperty();

            PathBuilder oderByExpression = new PathBuilder(Board.class,"board");

            tuple.orderBy(new OrderSpecifier(direction,oderByExpression.get(prop)));
        });
        tuple.groupBy(board);



        return null;
    }
}
