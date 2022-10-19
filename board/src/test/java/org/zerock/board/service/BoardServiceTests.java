package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@Log4j2
@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService service;

    @Test
    void testRegister() {
        log.info("register Test!");

        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Content!")
                .writerEmail("user100@naver.com") // 현재 DB상 존재하는 email 을 넣어줘야함!
                                                 // 이유 :: Board 와 Member 의 연관관계를 생각하자
                .build();

        Long  bno = service.register(dto);

        log.info("result bno ::" + bno);

    }

    @Test
    void testList() {
        
        //pageRequestDTO 의 생성자에 의해서 page =1 , size = 10 으로 초기화된 객체변수 생성
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        
        //<입력값, 반환값>
        PageResultDTO<BoardDTO,Object[]> result = service.getList(pageRequestDTO);

        result.getDtoList().forEach(log::info);

    }
}
