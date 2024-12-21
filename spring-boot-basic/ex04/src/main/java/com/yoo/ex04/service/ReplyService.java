package com.yoo.ex04.service;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.entity.Board;
import com.yoo.ex04.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO); //Create

    List<ReplyDTO> getList(Long bno); //Read

    void modify(ReplyDTO replyDTO); //Update

    void delete(Long rno);//Delete

    //DTO -> Entity
    default Reply dtoToEntity(ReplyDTO replyDTO){
        return Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(Board.builder().bno(replyDTO.getBno()).build())
                .build();
    }

    //Entity -> DTO
    default ReplyDTO entityToDto(Reply reply){
        return ReplyDTO.builder()
                .rno(reply.getRno())
                .bno(reply.getBoard().getBno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }

}
