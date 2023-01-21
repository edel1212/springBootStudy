package com.yoo.toy.service;

import com.yoo.toy.dto.BoardDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.Member;
import com.yoo.toy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO requestDTO) {
        Function<Object[], BoardDTO>  fn
                = en -> this.entityToDTO( (Board)en[0], (Member)en[1], (Long) en[2]);
        Page<Object[]> result =
                boardRepository.getBoardWithReplyCount(
                        requestDTO.getPageable(Sort.by("bno").descending())
                );
        return new PageResultDTO<>(result,fn);
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getListWithJQPLQuery(PageRequestDTO requestDTO) {

        log.info("requestDTO ::: {}",requestDTO);
    
        // 1. Functional 변수 생성 - 해당 entity-> DTO 메서는 service에 정의되어 있음
        Function<Object[], BoardDTO> fn = en -> this.entityToDTO( (Board) en[0]
                                                                , (Member)en[1]
                                                                , (Long) en[2]);

        // 2 . JPQLQuery를 적용한 Method 호출
        Page<Object[]> result = boardRepository.searchPageWithSort(requestDTO.getType()
                , requestDTO.getKeyword()
                ,requestDTO.getPageable(Sort.by("bno").descending()) );

        return new PageResultDTO<>(result, fn);
    }
}
