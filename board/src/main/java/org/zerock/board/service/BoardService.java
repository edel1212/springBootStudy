package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {
    //등록
    public Long register(BoardDTO dto);
    
    //받아온 DTO 데이터를 --> Entity 객체로 변환
    default Board dtoToEntity(BoardDTO dto){

        //Writer 에 넣어줄 Member 개체생성
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        return Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member) // 해당 Member 객체는 Email 값만 갖고 있음
                .build();
    }
    
    //__Eof__
}
