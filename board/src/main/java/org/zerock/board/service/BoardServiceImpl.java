package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.BoardRepository;

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
}
