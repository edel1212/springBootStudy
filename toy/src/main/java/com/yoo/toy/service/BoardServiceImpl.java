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
}
