package com.yoo.toy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@Builder
public class PageRequestDTO {
    
    private int page; //현재 페이지
    private int size; //목록의 개수

    
    //NoArgsConstructor
    //- 값이 없을 경우 default 값 지정
    public PageRequestDTO(){
        this.page = 1;
        this.page = 10;
    }

    //Pageable 객체를 생성하는 매개변수로 Sort 값을 받는다.
    public Pageable getPageable(Sort sort){
        //PageRequest.of() 는 0 부터 시작하기에 -을 해줌
        return PageRequest.of(this.page - 1 , this.size, sort);
    }

}
