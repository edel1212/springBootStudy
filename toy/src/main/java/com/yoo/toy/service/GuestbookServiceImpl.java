package com.yoo.toy.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yoo.toy.dto.GuestBookDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.GuestBook;
import com.yoo.toy.entity.QGuestBook;
import com.yoo.toy.repository.GuestBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

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

        Page<GuestBook> result = guestBookRepository.findAll(this.getSearch(requestDTO),pageable);

        Function<GuestBook, GuestBookDTO> fn = this::entityToDto;

        return new PageResultDTO<GuestBookDTO, GuestBook>(result, fn);
    }


    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO){
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestBook qguestBook = QGuestBook.guestBook;

        if(StringUtils.isEmptyOrWhitespace(type)){
            return booleanBuilder;
        }

        if(type.contains("t")){
            booleanBuilder.or(qguestBook.title.contains(keyword));
        }
        if(type.contains("c")){
            booleanBuilder.or(qguestBook.content.contains(keyword));
        }
        if(type.contains("w")){
            booleanBuilder.or(qguestBook.writer.contains(keyword));
        }

        return booleanBuilder;
    }

}
