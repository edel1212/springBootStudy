package com.yoo.instarServer.service;

import com.yoo.instarServer.vo.BoardVO;

import java.util.List;

public interface BoardService {
    List<BoardVO> getBoard();

    int registerBoard(BoardVO boardVO);
}
