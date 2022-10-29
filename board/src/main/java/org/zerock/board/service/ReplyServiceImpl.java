package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.ReplyDTO;

import java.util.List;

@Service
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    @Override
    public Long register(ReplyDTO replyDTO) {
        return null;
    }

    @Override
    public List<ReplyDTO> getLost(Long bno) {
        return null;
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

    }

    @Override
    public void remove(Long rno) {

    }
}
