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

    /**
     * 아래와 같이 사용하면 Error 발생 단독으로 사용 불가능함.
     * Bean을 따로  추가해줘 Error 발생함 상속을 통해 사용하거나
     * 대상 JpaRepository에 따로 설정을 하는 방식으로 사용 필요
     * Error Msg : Error creating bean with name .........
     *              Unsatisfied dependency expressed through field
     */
    //@Autowired
    //private SearchBoardRepository searchBoardRepository;

    @Test
    public void getListWithSupportImpl() {
        boardRepository.search1();
    }

    @Test
    public void getListWithSupportImpl2() {
        boardRepository.search1();
    }
}

