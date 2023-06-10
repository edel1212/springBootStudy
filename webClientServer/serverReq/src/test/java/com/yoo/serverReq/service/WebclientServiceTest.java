package com.yoo.serverReq.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebclientServiceTest {

    @Autowired
    private WebclientService webclientService;

    @Test
    public void test1(){
        webclientService.getSensorList();
    }

}
