package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

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
            // ID가 맞는 Member 객체변수를 만들어줌
            Member member = Member.builder().email("user"+i+"@naver.com").build();
            
            Board board = Board.builder()
                    .title("Title.."+i)
                    .content("Content..."+i)
                    .writer(member) //상단에서 만들어줌 객체 변수를 넣어줌
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

    //__Eof__
}
