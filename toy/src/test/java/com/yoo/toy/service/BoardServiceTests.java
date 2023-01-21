package com.yoo.toy.service;

import com.yoo.toy.dto.BoardDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void getListTest(){
        // new PageRequestDTO() 기본 생성자를 넘기면
        // 생성자 매서드에서 ...of(0,1) 생성자 생성
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(new PageRequestDTO());

        result.getDtoList().forEach(log::info);
    }


    ////////////////////////////////////////


    //JPQLQuery With Sort and Search Option Test
    @Test
    @Description("JPQLQuery Sort And Search option")
    public void testSearchWithSortAndKeyword(){
        // 1. PageRequestDTO 객채생성
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setType("twc");
        pageRequestDTO.setKeyword("1");

        PageResultDTO<BoardDTO, Object[]> result = boardService.getListWithJQPLQuery(pageRequestDTO);

        result.getDtoList().forEach(log::info);

    }


}
