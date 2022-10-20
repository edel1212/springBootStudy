package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    public void getTest(){
        log.info("get Test");

        BoardDTO result = service.get(2L);
        log.info(result);

    }

    @Test
    @DisplayName("게시글 삭제 테스트 해당 bno는 존재해야함")
    public void testRemove(){
        log.info("delete Test");
        service.removeWithReplies(100L);
        /*
         === Result Query -- Reply 가 먼저 삭제된 후 Board가 삭제 된것을 볼 수 있다.
        * Hibernate:
        * delete
        *         from
        * reply
        *         where
        * board_bno=?
        * Hibernate:
        * select
        * board0_.bno as bno1_0_0_,
        *         board0_.moddate as moddate2_0_0_,
        * board0_.regdate as regdate3_0_0_,
        *         board0_.content as content4_0_0_,
        * board0_.title as title5_0_0_,
        *         board0_.writer_email as writer_e6_0_0_
        * from
        * board board0_
        * where
        * board0_.bno=?
        * Hibernate:
        * delete
        *         from
        * board
        *         where
        * bno=?
        * */
    }

    @Test
    public void testModify(){
        log.info("modify!");
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목변경 테스트")
                .content("내용 변경 테스트")
                .build();

        service.modify(boardDTO);
    }

}
