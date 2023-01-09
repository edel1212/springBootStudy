package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void readTest1(){
        Optional<Board> result = boardRepository.findById(100L);
        result.ifPresent(log::info);
    }

}
