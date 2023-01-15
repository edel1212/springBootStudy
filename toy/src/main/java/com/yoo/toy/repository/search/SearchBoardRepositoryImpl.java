package com.yoo.toy.repository.search;

import com.querydsl.jpa.JPQLQuery;
import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.QBoard;
import lombok.extern.log4j.Log4j2;
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
@Component
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
}
