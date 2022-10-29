package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    
    //@RequiredArgsConstructor 를 통한 DI 주입
    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        //dto -> entity
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);
        
        //@GeneratedValue(strategy = GenerationType.IDENTITY) 로 생성된 SEQ 반환
        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {

        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());

        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

    }

    @Override
    public void remove(Long rno) {

    }
}
