package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
            //1부터 100까지 임의 번호
            long bno = 1L;//(long) ( Math.random() * 100 + 1 );

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
    public void restTest1(){
        Optional<Reply> result = replyRepository.findById(1L);
        result.ifPresent(log::info);
    }

}
