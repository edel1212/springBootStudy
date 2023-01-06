package com.yoo.toy.service;

import com.yoo.toy.dto.GuestBookDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.GuestBook;
import com.yoo.toy.repository.GuestBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestBookRepository guestBookRepository;

    @Override
    public Long register(GuestBookDTO dto) {
        log.info("DTO :: {}",dto);
        GuestBook guestBook = this.dtoToEntity(dto);
        log.info("GuestBook Entity :: {}", guestBook);

        guestBookRepository.save(guestBook);

        return guestBook.getGnum();
    }

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gnum").descending());

        Page<GuestBook> result = guestBookRepository.findAll(pageable);

        Function<GuestBook, GuestBookDTO> fn = this::entityToDto;

        return new PageResultDTO<GuestBookDTO, GuestBook>(result, fn);
    }


}
