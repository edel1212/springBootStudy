package com.yoo.ex04.repository;

import com.yoo.ex04.entity.Board;
import com.yoo.ex04.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testListByBoard(){
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(
                Board.builder().bno(90L).build()
        );

        replyList.forEach(log::info);
    }

}
