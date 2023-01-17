package com.yoo.toy.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.QBoard;
import com.yoo.toy.entity.QMember;
import com.yoo.toy.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return null;
    }
}
