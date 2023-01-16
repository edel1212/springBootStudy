package com.yoo.toy.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

@SpringBootTest
@Log4j2
public class SupportQueryTests {

    @Autowired
    private BoardRepository boardRepository;


    @Test
    @Description("JPQLQuery LeftJoin Test")
    void joinTest() {

        boardRepository.search2WithJoin();

    }
}
