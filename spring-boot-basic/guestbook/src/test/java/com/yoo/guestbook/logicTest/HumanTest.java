package com.yoo.guestbook.logicTest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class HumanTest {
    @Test
    public void test(){
        log.info("test!@");

        Human h = new Human();
        log.info(h);
        
        h.setName("흑곰");

        log.info(h);

        Human h2 = new Human("yoo",100);
        log.info(h2);

    }

}
