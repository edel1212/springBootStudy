package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    
    //Board Repository 주입
    private final BoardRepository repository;
    //Reply Repository 주입
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info("register Dto Data :: " + dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        //Object 에서 넘어온 값들인 배열순으로 { Board 객체 , Member 객체 , Long 댓글 수 } 이다.
        Function<Object[],BoardDTO> fn = (en-> entityToDTO( (Board)en[0]
                                                            ,(Member)en[1]
                                                            ,(Long)en[2]));

        Page<Object[]> result = repository.getBoardWithReplyCount(
                                                pageRequestDTO.getPageable(Sort.by("bno").descending())
                                            );


        return new PageResultDTO<>(result,fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        log.info("get Board" + bno);
        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0],(Member) arr[1],(Long) arr[2]);
    }

    @Transactional //Reply 에서 먼저 삭제 로직이 돌아야 하기떄문에 Transactional 이 필수이다!
    @Override
    public void removeWithReplies(Long bno) {
        //댓글을 먼저 삭제 해준다  -- > Reply Entity 가 Board Entity 를
        //                          FK 의존 관계이기 때문이다.
        replyRepository.deleteByBno(bno);
        //이후 개시글 삭제
        repository.deleteById(bno);

    }
}
