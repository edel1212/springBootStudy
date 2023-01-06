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
                .page(1).size(10).build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO =
                guestbookService.getList(pageRequestDTO);

        log.info(resultDTO.getDtoList());
    }

}
