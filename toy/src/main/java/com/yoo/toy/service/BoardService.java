package com.yoo.toy.service;

import com.yoo.toy.dto.BoardDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.Board;
import com.yoo.toy.entity.Member;

import java.awt.print.Pageable;

public interface BoardService {

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO requestDTO);

    //DTO 변환 시
    //각각해당하는 부분에 데이터를 주입하기위해서
    // 3개의 파라미터가 필요하다[ Board, Member, Long ]
    default BoardDTO entityToDTO(Board board, Member member,Long replyCnt){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCnt.intValue())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }

}
