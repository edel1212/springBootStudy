package org.zerock.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO,EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private int start, end;

    boolean prev, next;

    private List<Integer> pageList;
    
    
    //PageResultDTO 생성자  필수값값으로 Page, Function 을 요구함
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();

        this.makePageList(result.getPageable());
    }

    //페이지 목록을 만드느 Method
    private void makePageList(Pageable pageable){
        this.page= pageable.getPageNumber() + 1;
        this.size= pageable.getPageSize();
        
        //마지막 페이지 번호   : 반올림( 현재페이지/ 한번에 보여줄 목록 수 ) / 페이지 넘버 개수
        int tempEnd = (int) Math.ceil(this.page/ 10.0) * this.size;

        this.start = tempEnd - this.size - 1;

        this.prev = start > 1;

        this.end = Math.min(this.totalPage, tempEnd);

        this.next = this.totalPage > tempEnd;

        this.pageList = IntStream.rangeClosed(this.start, this.end).boxed()
                .collect(Collectors.toList());

    }


}
