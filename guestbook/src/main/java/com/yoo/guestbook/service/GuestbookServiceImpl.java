package com.yoo.guestbook.service;

import com.yoo.guestbook.dto.GuestbookDTO;
import com.yoo.guestbook.entitiy.Guestbook;
import com.yoo.guestbook.repository.GuestbookRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입 - final 이나 @Notnull 인 변수를 사용하여 생성자를 만듬
//@AllArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository;
    //@Autowired
    //private  GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO---------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }
}
