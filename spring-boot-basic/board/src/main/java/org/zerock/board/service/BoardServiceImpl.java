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

    /***
     * 한번에 실행되어야 하는 트랜잭션이기에
     * @Transactional 어노테이션은 필수이다
     * 
     */
    @Transactional //필수
    @Override
    public void removeWithReplies(Long bno) {
        /**
         * 댓글을 먼저 삭제 해준다  - 이유❔ ::  Reply Entity[FK] 이기 때문
         * - 중요 : 해당 deletByBno의 경우 @Modifying 어노테이션이 추가되어있음
         */
        replyRepository.deleteByBno(bno);

        //Reply 삭제 후 Board 삭제
        /***
         * - 상당 Reply의 경우 JPQL을 사용했기에 [해당 Repository에]@Modifying를 추가해줘야했지만
         * - 아래와 같은 경우 Jpa 내장 Method를 사용하므로 추가해줄 필요가 없었다!
         */
        repository.deleteById(bno);

    }

    /**
     * @Modifying을 사용하지 않아도 되는 이유는
     * save()의 경우 jpa 기본 메서드이기 떄문이다
     * ✅ 간단 설명 :: JPQL을 사용하지 않기 때문임!
     */
    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board = repository.findById(boardDTO.getBno());

        // 해당 Board의 존재 유무를 확인
        // save()의 경우 Insert, Update 기능을 한번에 하기 떄문이다.
        if(board.isPresent()){
            Board result = board.get();
            // Board Entity Class에서 만들어준 값 변경 
            // Method 사용
            result.changeTitle(boardDTO.getTitle());
            result.changeContent(boardDTO.getContent());
            repository.save(result);
        }
    }
}
