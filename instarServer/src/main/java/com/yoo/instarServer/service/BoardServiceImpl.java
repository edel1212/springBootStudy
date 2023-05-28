package com.yoo.instarServer.service;

import com.yoo.instarServer.mapper.board.BoardMapper;
import com.yoo.instarServer.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{


    private final BoardMapper boardMapper;

    @Override
    public List<BoardVO> getBoard() {
        return boardMapper.getBoard();
    }

    @Override
    public int registerBoard(BoardVO boardVO) {
        return boardMapper.registerBoard(boardVO);
    }
}
