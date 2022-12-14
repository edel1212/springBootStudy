package com.yoo.guestbook.service;

import com.yoo.guestbook.dto.GuestbookDTO;
import com.yoo.guestbook.dto.PageRequestDTO;
import com.yoo.guestbook.dto.PageResultDTO;
import com.yoo.guestbook.entitiy.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(long gno);

    void modify(GuestbookDTO guestbookDTO);

    /**
     * @Description : Entity 객체를 DTO 객체로 변환하기 위해 사용함! - Java8 버전 부터 추가된
     *                defualt를 사용 기존에 추상 클래스를 통해서 전달해야하는 실제 코드를 
     *                인터페이스에 선언이 가능해짐
     *                
     *                - ModelMapper 혹은 MaoStruct 등의 라이브러리를 사용할 수도 있지만
     *                - 해당 예제에서는 default method 를 사용하여 직접 처리함
     *
     *                - DTO(Data Transfer Object)로 변환하는 이유 ?
     *                   1) DTO 는 Entity 객체와 달리 각 계층끼리 주고받는 우편물이나 상자의 개념
     *
     *                   2) JPA를 이용하게되면 엔티티 객체는 단순히 데이터를 담는 객체가 아니기에
     *                      이것을 분리하기위해 사용
     *
     *                   3) DTO는 일회성으로 데이터를 주고받는 용도지만 Entity 객체는 엔티티메니저가
     *                      데이터를 관리함 따라서 생명주기도 전혀 다름!
     * */
    default Guestbook dtoToEntity(GuestbookDTO dto){
        return Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }

    default  GuestbookDTO entityToDto(Guestbook entity){
        return GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegeDate())
                .modDate(entity.getModDate())
                .build();
                
    }

}
