package com.yoo.ex04.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;
    private int size;
    
    //검색조건
    private String type;
    private String keyword;

    /**
     * @Description : 기본 생성자로 기본 페이지 1과 size 를 10으로 지정함
     * */
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    /**
     * @Description : 페이징에 사용될 Method
     * */
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size , sort);
    }

}
