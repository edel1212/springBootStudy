package com.yoo.guestbook.dto;


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

    private int page; //현재 페이지
    private int size; //목록의 개수
    
    //검색조건
    private String type;
    private String keyword;

    //NoArgsConstructor
    //- 값이 없을 경우 default 값 지정
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    //Pageable 객체를 생성하는 매개변수로 Sort 값을 받는다.
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size , sort);
    }

}
