package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {
    //등록
    public Long register(BoardDTO dto);

    //페이징 목록
    PageResultDTO<BoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);

    //단건 조회
    BoardDTO get(Long bno);
    
    //삭제 -- ✔ Reply ReplyRepository 에서 삭제 Method가 필요함
    void removeWithReplies(Long bno);

    //게시글 수정
    void modify(BoardDTO boardDTO);

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
    
    // DTO 변환 시
    // 각각해당하는 부분에 데이터를 주입하기위해서
    // 3개의 파라미터가 필요하다[ Board, Member, Long ]
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail()) // Member 객체에서 받아옴
                .writerName(member.getName())  // Member 객체에서 받아옴
                .replyCount(replyCount.intValue()) //Parameter 의 Type이 Long으로 받아오기에 변환해줌 
                .build();
    }
    
    //__Eof__
}
