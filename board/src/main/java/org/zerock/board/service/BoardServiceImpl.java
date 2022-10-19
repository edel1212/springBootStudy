package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.BoardRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;


    @Override
    public Long register(BoardDTO dto) {

        log.info("register Dto Data :: " + dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        //Object 에서 넘어온 값들인 배열순으로 { Board 객체 , Member 객체 , Long 댓글 수 } 이다.
        Function<Object[],BoardDTO> fn = (en-> entityToDTO( (Board)en[0]
                                                            ,(Member)en[1]
                                                            ,(Long)en[2]));

        Page<Object[]> result = repository.getBoardWithReplyCount(
                                                pageRequestDTO.getPageable(Sort.by("bno").descending())
                                            );


        return new PageResultDTO<>(result,fn);
    }
}
