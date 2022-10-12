package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){

        IntStream.rangeClosed(1,300).forEach(i->{
            //1부터 100까지 임의 번호
            long bno = (long) ( Math.random() * 100 + 1 );

            //Board 객체 변수를 만들어줌 PK가 있어야함! 
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

    //__Eof__
}
