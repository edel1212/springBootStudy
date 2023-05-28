package com.yoo.instarServer.mapper.board;

import com.yoo.instarServer.vo.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVO> getBoard();

    int registerBoard(BoardVO boardVO);
}
