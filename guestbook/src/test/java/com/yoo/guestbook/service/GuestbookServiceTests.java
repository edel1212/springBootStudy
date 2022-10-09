package com.yoo.guestbook.service;

import com.yoo.guestbook.dto.GuestbookDTO;
import com.yoo.guestbook.dto.PageRequestDTO;
import com.yoo.guestbook.dto.PageResultDTO;
import com.yoo.guestbook.entitiy.Guestbook;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService guestbookService;

    @Test
    public void testRegister(){
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user0")
                .build();
        log.info(guestbookService.register(guestbookDTO));
    }
    
    /**
     * @Description  : 해당 테스트를 보면 재사용을 위해 Page에 관련된 Class를 나눠놓은것 을 볼수 있는데
     *                 각각 의 Class의 흐름을 보자
     *
     *                 1) GuestBook - Entity Class 로 객체형태로 Table 의 형태를 가지고 있다 ORM 을 사용해서 변환됨
     *
     *                 2) GuestBookDTO - DTO Class 는 Entity Class 를 포장해주는 Class 로 이해하면 된다
     *                                   직접적으로 Entity class 를 건드는 것이 아닌 DTO 를 사용하여 좀 더 본직에 맞게
     *                                    개발하기 위해 나눠 사용한 것
     *
     *                 3) PageRequestDTO - * 해당 Class 는 페이징에 사용될 page 번호와 한펜이지에서 보여줄 size 를 지정해줌
     *                                       기본생성자로 사용시 page:1 , size:10 으로 초기화 하여 사용함
     *                                     * method 로 getPageable 이 있으며 해당 method 는 Pageable 을 반환하기 위해 사용
     *                
     *                4) PageResultDTO - * 해당 class 는 페이징의 결과를 담을 DTO Class 이며
     *                                     제네릭으로<DTO, EN>을 받는다 (DTOClass, EntityClass)
     *                                   * 그후 생성자로 PageResultDTO(Page<EN> result, Function<EN,DTO> fn)를 받아
     *                                     this.dtoList = result.stream().map(fn).collect(Collectors.toList());
     *                                     dtoList를 초기화 해줌
     *                                   
     *                                     
     * */
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        resultDTO.getDtoList().forEach(log::info);

    }

    @Test
    public void testListWithPaging(){
        //PageRequestDTO 객체를 만들어줌 현재 페이지 1 , size 10
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
    
        //PageResultDTO<EntityDTO 클래스 , Entity 클래스> 형태의 객체를  service 를 통해 만들어줌
        PageResultDTO<GuestbookDTO,Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        log.info("Prev : "+ resultDTO.isPrev());// 이전 버튼 
        log.info("Next : "+ resultDTO.isNext());// 다음 버튼
        log.info("TotalPageNum : "+ resultDTO.getTotalPage());// 총페이지 개수

        log.info("-------------------------------------");
        //DTO (Index 별 데이터)목록
        resultDTO.getDtoList().forEach(log::info);


        log.info("==========================================");
        //페이징번호목록
        resultDTO.getPageList().forEach(log::info);
    }


}
