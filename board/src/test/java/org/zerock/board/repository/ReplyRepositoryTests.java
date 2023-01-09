package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){

        IntStream.rangeClosed(1,300).forEach(i->{
            //Board의 존재하는 bno [ 1 ~ 100]으로 만들었음
            long bno = 1L;//(long) ( Math.random() * 100 + 1 );

            //Board 객체 생성
            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder().text("ReplyText..."+i)
                    .board(board) // 상단에서 만든 객체변수 주입
                    .replyer("guest"+i)
                    .build();

            replyRepository.save(reply);

        });

    }

    @Test
    public void findReply(){
        List<Reply> result = replyRepository.findAll();
        result.forEach(log::info);
    }


    @Test
    public void readReply1(){
        /*
        *  - Result Query
        *    Hibernate:
        *     select
        *         reply0_.rno as rno1_2_0_,
        *         reply0_.board_bno as board_bn4_2_0_,
        *         reply0_.replyer as replyer2_2_0_,
        *         reply0_.text as text3_2_0_,
        *         board1_.bno as bno1_0_1_,
        *         board1_.moddate as moddate2_0_1_,
        *         board1_.regdate as regdate3_0_1_,
        *         board1_.content as content4_0_1_,
        *         board1_.title as title5_0_1_,
        *         board1_.writer_email as writer_e6_0_1_,
        *         member2_.email as email1_1_2_,
        *         member2_.moddate as moddate2_1_2_,
        *         member2_.regdate as regdate3_1_2_,
        *         member2_.name as name4_1_2_,
        *         member2_.password as password5_1_2_
        *     from
        *         reply reply0_
        *     left outer join
        *         board board1_
        *             on reply0_.board_bno=board1_.bno
        *     left outer join
        *         member member2_
        *             on board1_.writer_email=member2_.email
        *     where
        *         reply0_.rno=?
        *
        *         ---------------------------------------------------------------
        *
        *     ✔ 위와같이 불필요하게 reply, board, member 까지 전부 left outer join처리 되어있는것을
        *        확인 할수 있는데 굉장히 비효율적이다
        *        => 예상했던것은 Board만 left outer join이었음
        *
        *     ✔ 위와 같은 특정한 엔티티를 조회할 때 연관관계를 가진 모든 엔티티를 같이
        *        로딩하는 것을 [ ✔중요 : "Eager loading"(즉시 로딩) ] 이라고 한다
        *
        *     ✔ 즉시 로딩은 한번에 연관관계가 있는 모든 엔티티를 가져온다는
        *        장점이 있지만 여러관계를 맺고 있거나 연관관계가 복잡할수록 불필요한 조인이 늘어나
        *        성능 저하 이슈가 있음
        *
        *     ✔ JPA 에서 연관관계 데이터를 어떻게 가져올 것인가를 [ ✔중요 : fetch(패치) ]라고 하며
        *       연관관계의 어노테이션 속성으로 "fetch 모드" 지정이 가능하다
        *       => ex) @ManyToOne (fetch = FetchType.???)
        *
        *     🎈 위와 같은 Eager loading(즉시 로딩)은 불필요한 조인까지 처리해야하는 경우가
        *        많기 때문에 가능한 사용하지 않고 그와 반대 개념으로
        *        [ ✔중요 : "지연로딩"(Lazy loading) ] 으로 처리해 주자
        *        -  Board Class 수정이 필요 @ManyToOne (fetch = FetchType.LAZY) 추가
        *        -  지연로딩처리를 한 class 의 정보를 가져오는 Service 에는
        *           @transaction 처리가 필수이다!
        *          ❌ 없을경우 no-session Error [ @see : BoardRepositoryTests.java ]
        *          --> 해당 오류의 이유는 board.getWriter()는 member 테이블을 로딩해야 하는데
        *              이미 데이터베이스와의 연결이 끝난 상태이기 때문이다. --- Lazy loading!!
        *
        *          --> 이러한 문제를 해결하기 위해서는 다시 한번 데이터베이스와 연결이 필요한데
        *              이럴때 @Transactional으로 해결이 가능하다.
        *
        * **/
        Optional<Reply> result = replyRepository.findById(1L);
        if(result.isPresent()){
            Reply reply = result.get();
            log.info(reply);
            log.info(reply.getBoard());
        }

    }

    @DisplayName("메서드 명으로 쿼리 생성")
    @Test
    public void testListByBoard(){
        log.info("Used Method Query");

        List<Reply> replyList = replyRepository.getReplyByBoardOrderByRno(
                Board.builder().bno(96L).build()
        );
        log.info(replyList);
    }



    //__Eof__
}
