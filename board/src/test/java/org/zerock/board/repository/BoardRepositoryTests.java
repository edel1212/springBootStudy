package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1,100).forEach(i->{
            // Member ID가 존재하는 Member 객체변수를 만들어야한다.
            Member member = Member.builder().email("user"+i+"@naver.com").build();
            
            Board board = Board.builder()
                    .title("Title.."+i)
                    .content("Content..."+i)
                    .writer(member) //상단에서 만들어준 Member객체를 넣어줌
                    .build();

            boardRepository.save(board);//완성된 Board 객체변수를 argument로 넣어줌
            
        });
    }

    @Test
    public void findBoard(){
        List<Board> result = boardRepository.findAll();
        result.forEach(log::info);

    }

    @Test
    @Transactional //Board Class 의 fetch 방식 변경을 했기 때문임
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L); // 100번의 Board Data
        Board board = new Board();
        if(result.isPresent()) {
            board = result.get();
        }

        //해당 데이터의 Query 를 보면 자동으로 join이 된것을 확인 할 수있다
        // ✔ 이유는 해당 Board 에 @ManyToOne 으로 FK 관계 형성을 해주었기 때문임!!
        log.info(board);

        log.info(board.getWriter());
        /* result Query
        select
        board0_.bno as bno1_0_0_,
                board0_.moddate as moddate2_0_0_,
        board0_.regdate as regdate3_0_0_,
                board0_.content as content4_0_0_,
        board0_.title as title5_0_0_,
                board0_.writer_email as writer_e6_0_0_,
        member1_.email as email1_1_1_,
                member1_.moddate as moddate2_1_1_,
        member1_.regdate as regdate3_1_1_,
                member1_.name as name4_1_1_,
        member1_.password as password5_1_1_
                from
        board board0_
        left outer join
        member member1_
        on board0_.writer_email=member1_.email*/

    }

    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);
        //배열 형식으로 파싱해줘야 사용이 가능하다!
        Object[] arr = (Object[]) result;

        log.info("------------------------");
        log.info(Arrays.toString(arr));
        // JPQL의 장점인 Object 타입으로 반환 받기에
        // 배열 [0] : Board , [1] Member 형태로 나옴!
        //[Board(bno=100, title=Title..100, content=Content...100), Member(email=user100@naver.com, password=111, name=Yoo100)]
    }

    @Test
    public void testGetBoardWithReply(){
        log.info("with Reply Test ! ::: Use join on!!");

        /*
         * 자료 형태
         * [ [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=55, text=ReplyText...55, replyer=guest55)]
         *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=71, text=ReplyText...71, replyer=guest71)]
         *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=211, text=ReplyText...211, replyer=guest211)]
         *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=248, text=ReplyText...248, replyer=guest248)] ]
         * */
        List<Object[]> result = boardRepository.getBoardWithReply(100L); // 100번의 번호를 갖는 Board + Reply

        result.stream()
                .map(Arrays::toString)
                .forEach(log::info);
        /*  - Result Query
         *
         * select
         * board0_.bno as bno1_0_0_,
         *         reply1_.rno as rno1_2_1_,
         * board0_.moddate as moddate2_0_0_,
         *         board0_.regdate as regdate3_0_0_,
         * board0_.content as content4_0_0_,
         *         board0_.title as title5_0_0_,
         * board0_.writer_email as writer_e6_0_0_,
         *         reply1_.board_bno as board_bn4_2_1_,
         * reply1_.replyer as replyer2_2_1_,
         *         reply1_.text as text3_2_1_
         * from
         * board board0_
         * left outer join
         * reply reply1_
         * on (
         *         reply1_.board_bno=board0_.bno
         * )
         * where
         * board0_.bno=?
        */
    }

    @Test
    public void testWithReplayCount(){
        Pageable  pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().map(Arrays::toString).forEach(log::info);

    }

    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);

        //들어온 result 는  배열 형식으로 각각의 값이 들어간다 (Board b , Member w, Reply r )
        Object[] arr = (Object[]) result;
        Arrays.stream(arr).forEach(log::info);
    }

    @Test
    public void searchTest1(){
        boardRepository.search1();
    }

    @Test
    public void searchWithLeftJoinTest(){
        boardRepository.search2WithJoin();
    }

    @Test
    public void searchWithTuple(){
        boardRepository.search3WithJoin();
    }

    ////////////////////////////////////////////////

    //JPQL + Querydls Paging test
    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0
                                                ,10
                                                , Sort.by("bno").descending()
                                                    .and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t","1",pageable);
    }

    //__Eof__
}
