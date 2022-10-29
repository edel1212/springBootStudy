package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Reply;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testGetList(){

        Long bno = 1L; //현재 DB 있는 board - bno

        List<ReplyDTO> result = replyService.getList(bno);

        result.forEach(log::info);

    }

}
