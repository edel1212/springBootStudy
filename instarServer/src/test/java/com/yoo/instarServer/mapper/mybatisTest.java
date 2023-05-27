package com.yoo.instarServer.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mybatisTest {

    @Autowired
    private Testyoo testyoo;

    @Test
    public void test1(){
        System.out.println(testyoo.getName());
    }

}
