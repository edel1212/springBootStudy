package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
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
    @Transactional
    public void readTest1(){
        Optional<Board> result = boardRepository.findById(100L);
        //result.ifPresent(log::info);

        if(result.isPresent()){
            log.info(result.get());
            log.info(result.get().getWriter()); // Transactional 이 없을시 Error
        }

    }


    /////////////////////////////////////////////
    // JPQL [ JOIN Test]
    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);
        log.info(result);
        log.info("-----------------------------------------");
        Object[] arr = (Object[])result;
        log.info(Arrays.toString(arr));
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

    }


    @Test
    public void testBoardWithReplyCnt(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.stream().map(Arrays::toString).forEach(log::info);
    }

}
