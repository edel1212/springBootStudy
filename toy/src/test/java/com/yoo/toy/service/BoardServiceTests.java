package com.yoo.toy.service;

import com.yoo.toy.dto.BoardDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void getListTest(){
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(new PageRequestDTO());

        result.getDtoList().forEach(log::info);
    }


}
