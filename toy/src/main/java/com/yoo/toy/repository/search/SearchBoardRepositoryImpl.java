package com.yoo.toy.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.QBoard;
import com.yoo.toy.entity.QMember;
import com.yoo.toy.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
*  1 . QuerydslRepositorySupport를 상속 받는다.
 * 2. 부모 Class 인 QuerydslRepositorySupport 에 생성자가 요구하는 값을 추가해준다.
 *    -> Class를 요구하는데 내가 원하는 <strong> 데이터 주체의 Entity Class 이다!</strong>
 * 3. 구현하고자 하는 interface를 impl 시킨 후 구현해준다.
* */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        //내가 사용할 Entity Class
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("Querydsl Support Search Method!!! ");

        //1 . Q도메인 생성
        QBoard board = QBoard.board;

        //2. 사용할 대상을 JPQLQuery으로 지정
        JPQLQuery<Board> jpqlQuery = from(board);
        
        //3. 필요 질의 조건 추가
        jpqlQuery.select(board).where(board.bno.eq(100L));
        
        //fetch()를 통해 데이터 빈환
        List<Board> result = jpqlQuery.fetch();
        
        return null;
    }


    //JPQL Query LeftJoin
    @Override
    public Board search2WithJoin() {

        log.info("-------------------------");

        //1 . Q도메인 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;
        
        //2. JPQLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);
        // 🎈 중요 : JPQL과 다른 점은 join에  연관관계가 있을 경우에도 on 으로 조건이 필요하다.
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member,  reply.count()).groupBy(board);

        List<Tuple> result = tuple.fetch(); 
        
        //<Tuple>을 사용 시
        //result.get(0).get(??) 이런식으로 접근이 가능해짐!

        log.info("result :: {}", result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("Search--------------------------");

        //1. Q도메인 객체 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JQPLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);

        /**
         * ❌ 삽질함 ..
         * ErrorMsg : No data type for node...
         *  이유 : jpqlQuery.leftJoin(board).on(board.writer.eq(member));
         *  수정 : jpqlQuery.leftJoin(member).on(board.writer.eq(member));
         *
         *  원인분석 : 이미 JQPLQuery 객체를 만들때 기준을 board로 잡았는데
         *            join에 또 board를 할필요가없다..
         *            ex) JPQLQuery<Board> jpqlQuery = from(board);
         * */
        //3. Join 추가
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //4. Generic을 Tuple로 변경 - 반환값의 객체가 board뿐만이 아니기 떄문임.
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        //3. 인자로 받아온 조건 추가 - BooleanBuilder, BooleanExpression
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(board.bno.gt(0L)); // bno가 0보다 크고
        
        //4. type 체크 알맞은 조건 추가
        if(type != null){
            if(type.contains("t")) booleanBuilder.or(board.title.contains(keyword));
            if(type.contains("w")) booleanBuilder.or(member.email.contains(keyword));
            if(type.contains("c")) booleanBuilder.or(board.content.contains(keyword));
        }//if

        //5. 3~4번에서 만들어준 조건을 typle에 추가
        tuple.where(booleanBuilder);

        //6. count를 사용했으니 board에 groupBy 시켜줌
        tuple.groupBy(board);
        
        //7. 타입을 List<Tuple>로 변경
        List<Tuple> result = tuple.fetch();

        log.info("result :: {}", result);

        return null;
    }

    @Override
    public Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable) {

        log.info("search With Sort");

        //1. Q도메인 객체 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JQPLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);

        //3. Join 추가
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //4. Generic을 Tuple로 변경
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        //3. 인자로 받아온 조건 추가 - BooleanBuilder, BooleanExpression
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(board.bno.gt(0L)); // bno가 0보다 크고

        //4. type 체크 알맞은 조건 추가
        if(type != null){
            if(type.contains("t")) booleanBuilder.or(board.title.contains(keyword));
            if(type.contains("w")) booleanBuilder.or(member.email.contains(keyword));
            if(type.contains("c")) booleanBuilder.or(board.content.contains(keyword));
        }//if

        //5. 3~4번에서 만들어준 조건을 typle에 추가
        tuple.where(booleanBuilder);

        //////////////////////////////////////
        // - 정렬 추가 -  //
        //tuple.orderBy(pageable.getSort()); ❌  Parameter가 다르므로 사용 불가능 !!

        // ✅ 해결 방법 
        // Pageable에서 받아온 sort 을 사용하여 OrderSpecifier 객체를 만들어 추가해준다.

        Sort sort = pageable.getSort();
        //정렬 조건이 여러개일수 있기에 forEach 사용
        sort.stream().forEach( order->{
            // i. 정렬 조건 값을 가져옴
            String prop = order.getProperty();
            // ii. parable에서 정렬 방식 확인 후 Order 객체 생성
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            // iii. PathBuilder 객체 생성 - [ 🎈주의 : Generic으로는 Entity Class를 사용해야한다! ]
            PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class,"board");
            // iv. orderBy()에 맞는 Parameter 사용
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
            
        });
        
        //////////////////////////////////////

        //6. count를 사용했으니 board에 groupBy 시켜줌
        tuple.groupBy(board);

        //7. 타입을 List<Tuple>로 변경
        List<Tuple> result = tuple.fetch();

        log.info("result :: {}", result);

        return null;
    }
}
