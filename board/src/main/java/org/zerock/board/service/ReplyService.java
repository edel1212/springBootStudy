package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {
    
    //등록
    Long register(ReplyDTO replyDTO);
    
    //Bno에 맞는 Reply  List
    List<ReplyDTO> getList(Long bno);
    
    //수정
    void modify(ReplyDTO replyDTO);
    
    //삭제
    void remove(Long rno);

    // DTO 객체 --> Entity 객체 변환 Default Method
    default Reply dtoToEntity(ReplyDTO replyDTO){
        // Parameter로 받아온 ReplyDTO 내 bno를 기준으로 Board 객체 생성
        Board board = Board.builder().bno(replyDTO.getBno()).build();

        return Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();
    }
    
    // Entity 객체 --> DTO 객체로 전환
    default ReplyDTO entityToDTO(Reply reply){
        // DTO 자체는 데이터 이동에 관여하는 객체이므로
        // 따로 Board 객체를 생성 후 주입해줄 필요가 없다
        return ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegeDate())
                .modDate(reply.getModDate())
                .build();
    }
    
}
