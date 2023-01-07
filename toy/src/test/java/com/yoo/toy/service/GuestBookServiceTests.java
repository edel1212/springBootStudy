package com.yoo.toy.service;

import com.yoo.toy.dto.GuestBookDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.GuestBook;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class GuestBookServiceTests {

    @Autowired
    private GuestbookService guestbookService;

    @Test
    public void registerTestWithDTO(){

        GuestBookDTO dto = GuestBookDTO.builder()
                .title("DTO Test!")
                .content("TestDTO!!")
                .writer("흑곰!")
                .build();

        guestbookService.register(dto);

    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2).size(5).build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO =
                guestbookService.getList(pageRequestDTO);

        log.info("Start :: {}",resultDTO.getStart());
        log.info("End :: {}",resultDTO.getEnd());
        log.info("Prev :: {}",resultDTO.isPrev());
        log.info("Next :: {}",resultDTO.isNext());
        log.info("Total :: {}",resultDTO.getTotalPage());
        log.info("PageNumList :: {}", resultDTO.getPageList());

        log.info("List :: {}",resultDTO.getDtoList());
    }

}
