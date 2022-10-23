package org.zerock.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.board.entity.Board;

public interface SearchBoardRepository {
    Board search1();

    Board search2WithJoin();

    Board search3WithJoin();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
