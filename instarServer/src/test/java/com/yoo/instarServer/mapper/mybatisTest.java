package com.yoo.instarServer.mapper;

import com.yoo.instarServer.mapper.user.TestAge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mybatisTest {
    @Autowired
    private TestAge testAge;

    @Test
    public void test2(){
        System.out.println(testAge.getAge());
    }
}
