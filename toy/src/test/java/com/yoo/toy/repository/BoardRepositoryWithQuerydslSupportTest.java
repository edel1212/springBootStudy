package com.yoo.toy.repository;

import com.yoo.toy.repository.search.SearchBoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardRepositoryWithQuerydslSupportTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SearchBoardRepository searchBoardRepository;

    @Test
    public void getListWithSupportImpl() {
        boardRepository.search1();
    }

    @Test
    public void getListWithSupportImpl2() {
        searchBoardRepository.search1();

    }
}

