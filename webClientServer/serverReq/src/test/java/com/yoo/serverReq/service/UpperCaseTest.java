package com.yoo.serverReq.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpperCaseTest {

    @Autowired
    private UpperCamelService upperCamelService;


    @Test
    public void testCode1(){
        upperCamelService.getItem(15L);
    }


    @Test
    public void testCode2(){
        upperCamelService.getList();
    }

}
