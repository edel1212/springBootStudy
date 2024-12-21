package com.yoo.ex04.service;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.entity.Board;
import com.yoo.ex04.entity.Reply;
import com.yoo.ex04.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = this.dtoToEntity(replyDTO);
        replyRepository.save(reply);
        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {
        // bno를 넘겨주는것이 아닌 Board 객체를 넘겨줘야함!!
        // Reply Entity 자체가 bno를 가지고 있지 않고
        // Board를 ManyToOne으로 연관관계를 갖게 했음
        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());
        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        replyRepository.save(this.dtoToEntity(replyDTO));
    }

    @Override
    public void delete(Long rno) {
        replyRepository.deleteById(rno);
    }
}
