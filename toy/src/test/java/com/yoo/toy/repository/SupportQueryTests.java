package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    @Test
    @Description("Tuple Test")
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //  List가 아니라 Page이다!!
        Page<Object[]> result = boardRepository.searchPage("t","1",pageable);
    }

}
