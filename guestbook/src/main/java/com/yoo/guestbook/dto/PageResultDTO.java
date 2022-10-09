package com.yoo.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description  : 해당 Class 는 JPA를 이용하는 Repository에서  페이지 처리결과를 Page<Entity>로 처리결괄르 받게하기
 *                 위한 Class 이다 서비스 계층에서 이를 처리하기 위해 별도의 클래스를 만들어 사용하는것
 *                 
 *                 - Page<Entity>의 엔티티 객체들의 DTO 객체로 변환해서 자료구조로 담아줘야함
 *                 - 화면에 출력에 필요한 페이지 정보들을 구성해줘야함
 * */
@Data
public class PageResultDTO<DTO, EN> {

    //DTO list
    private List<DTO> dtoList;

    //Total Page Num
    private int totalPage;

    //Now page Num
    private int page;
    //List size
    private int size;

    //start, end Page Num
    private int start,end;

    //next, prev flag
    boolean prev,next;

    //page Num List
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1 ; // 0부터 시작하므로 1을 더해 줌
        this.size = pageable.getPageSize();// 현재 페이지 Size 세팅

        //temp end page
        //현재 page 에서 10.0을 나눈 후 그값을 올림처리 한 후에 * 내가 보여줄 Size 를 곱해줌
        // 1 페이지 경우  MathCeil(0.1) * 10 = 10
        // 10 페이지 경우  MathCeil(1) * 10 = 10
        // 100 페이지 경우  MathCeil(10) * 10 = 100
        int tempEnd = (int) Math.ceil(this.page/ 10.0) * this.size;

        start = tempEnd - (this.size-1) ; // 시작 페이지는 end page 에서 (size-1) 값임

        prev = start > 1; //이전 버턴은 1보다 클경우 true
        
        //마지막 번호는  total Page 와 tempEnd 를 비교하여 작은 수로 지정 이유는 
        // 진짜 totalNum 은 350이여서 " 35" 가  totalPage 가 되야하지만
        // tempEnd 의 경우 현재 내가 고른 페이지의 번호가 33 일경우 40으로 계산되기 때문에 그럴 경우
        //에 정합성이 맞지 않아서 해당 아래 로직이 필요
        end = Math.min(this.totalPage, tempEnd);

        //현재 페이지가 tempEnd 보다 많을경우 True
        //RealTotalPage = 13인데
        //tempEnd 는 10 일경우 당연히 3개의 페이지 개수가
        //더 있기 때문임!
        next = this.totalPage > tempEnd;

        this.pageList = IntStream.rangeClosed(start,end).boxed()
                .collect(Collectors.toList());
    }

}
