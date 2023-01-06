package com.yoo.toy.service;

import com.yoo.toy.dto.GuestBookDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.GuestBook;

public interface GuestbookService {
    //등록
    Long register(GuestBookDTO dto);


    PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);



    ////////////////////////////////////////////////////////////////


    default GuestBook dtoToEntity(GuestBookDTO dto){
        return GuestBook.builder()
                .gnum(dto.getGnum())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }

    default GuestBookDTO entityToDto(GuestBook guestBook){
        return GuestBookDTO.builder()
                .gnum(guestBook.getGnum())
                .title(guestBook.getTitle())
                .content(guestBook.getContent())
                .writer(guestBook.getWriter())
                .regDate(guestBook.getRegDate())
                .modDate(guestBook.getModDate())
                .build();
    }
}
