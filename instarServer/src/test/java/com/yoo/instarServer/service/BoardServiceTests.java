package com.yoo.instarServer.service;

import com.yoo.instarServer.vo.BoardVO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void getListTest(){
        log.info(boardService.getBoard());
    }

    @Test
    public void registerBoardTest(){
        IntStream.rangeClosed(1, 3).forEach((idx)->{
            BoardVO vo = BoardVO.builder()
                    .name("YooJH"+idx)
                    .userImage("https://placeimg.com/100/100/arch")
                    .postImage("https://placeimg.com/640/480/arch")
                    .likes(idx*23)
                    .liked(idx%2 == 0)
                    .content("오늘 무엇을 했냐면요 아무것도 안했어요 ?")
                    .filter("perpetua")
                    .build();
            boardService.registerBoard(vo);
        });

    }
}
