package com.yoo.toy.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO <DTO, EN>{ // Generic으로 DTO와, Entuty를 받음 

    // 실제 사용 데이터
    private List<DTO> dtoList;

    //총 페이지 개수
    private int totalPage;
    
    //현재 페이지 번호
    private int page;

    //목록 사이즈
    private int size;

    //시작 페이지번호
    private int start;

    //끝 페이지번호
    private int end;

    //이전 Flag
    private boolean prev;

    //다음 FLag
    private boolean next;

    //페이지 번호 목록
    private List<Integer> pageList;

    //생성자로는 Page 와 처리할 Function을 매개변수로 받는다
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
        this.totalPage = result.getTotalPages();

        this.makePageList(result.getPageable());
    }
    
    // 페이지목록을 만드는 Method
    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; //현재 페이지번호 [ 0부터 시작이므로 +1 필수 ]
        this.size = pageable.getPageSize();   //페이지 Size
        
        //Temp End Number ( StartPageNum 을 구하는 목적이 큼 )
        // 10.0을 나누는것은 고정 - > 올림처리 -> * 10
        int tmpEnd = (int)Math.ceil(this.page / 10.0) * 10;
        
        // tmpEnd 번호 - 9 = 스타트 번호가 나옴
        this.start = tmpEnd - 9;

        // 시작 번호가 1보다 크면 이전버튼이 있어야함
        this.prev = start > 1;

        // 다음페이지 버튼은 총피이지 번호 ">" tmpEnd
        // 이유 :  totalPage는 전체 개수이지만
        //        tmpEnd는 무조건 Size()설정대로 무조건 단위에 맞춰 나오게 계산하기  떄문이다.
        this.next = this.totalPage > tmpEnd;


        // 둘중 작은걸 쓰면 된다.
        // tmpEnd의 경우는 무조건 자리수에 맞춰 계산하기에
        // 잘못된 수가 나오기 때문임
        // ex)
        // this.totalPage > tmEnd ? tmpEnd : this.totalPage
        this.end = Math.min(tmpEnd, this.totalPage);

        this.pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }

}
