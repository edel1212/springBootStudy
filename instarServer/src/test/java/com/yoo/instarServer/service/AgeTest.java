package com.yoo.instarServer.service;

import com.yoo.instarServer.mapper.user.TestAge;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AgeTest {

    @Autowired
    private YooService yooService;


    @Test
    public  void test(){
        log.info(yooService.getAage());
    }

}
