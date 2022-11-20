package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder
@Data
@AllArgsConstructor
/*
 * @NoArgsConstructor 를 추가하 하지 않는 이유는
 * 아무것도 받지 안흔 기본 생성자를 내가 작성 후
 * 기본값을 지정해주기 떄문이다
 * */
public class PageRequestDTO {

    private int page;

    private int size;

    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }

}
