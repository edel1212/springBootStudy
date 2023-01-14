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
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.Optional;
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

        // DTO로 변환 함수 인데 여기서 Object[]  Index 순서는
        // 내가 JPQL에 작성한 쿼리의 순서이다 @See : boardRepository.java
        // ex) SELECT b, w, count(r) ...
        Function<Object[],BoardDTO> fn 
                        = (en-> this.entityToDTO( (Board)en[0], (Member)en[1], (Long)en[2]));

//페이징이랑 연결되어있지 않은 repository Method
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                                                pageRequestDTO.getPageable(Sort.by("bno").descending())
//                                            );

        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType()
        ,pageRequestDTO.getKeyword()
        ,pageRequestDTO.getPageable(Sort.by("bno").descending())
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

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board = repository.findById(boardDTO.getBno());

        if(board.isPresent()){
            Board result = board.get();
            result.changeTitle(boardDTO.getTitle());
            result.changeContent(boardDTO.getContent());
            repository.save(result);
        }


    }
}
