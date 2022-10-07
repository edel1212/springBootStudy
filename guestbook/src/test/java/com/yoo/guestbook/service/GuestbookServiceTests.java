package com.yoo.guestbook.service;

import com.yoo.guestbook.dto.GuestbookDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService guestbookService;

    @Test
    public void testRegister(){
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title")
                .content("Sample Conetent")
                .writer("user0")
                .build();
        log.info(guestbookService.register(guestbookDTO));
    }

}
