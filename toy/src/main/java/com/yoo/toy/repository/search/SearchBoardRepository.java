package com.yoo.toy.repository.search;

import com.yoo.toy.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    Board search1();


    Board search2WithJoin();

    // 파라미터로 PageRequestDTO를 사용하지 않는 이유는
    // Repository에서 DTO의 사용의도를 생각해보면 쉽다
    // DTO(Data Transfer Object)란
    // 말 그대로 계층간 데이터 교환을 위해 사용하는
    // 객체(Java Beans) 이기에 취지에 적합하지 않기 때문이다.
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
