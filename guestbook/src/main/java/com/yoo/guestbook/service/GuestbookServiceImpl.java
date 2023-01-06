package com.yoo.guestbook.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yoo.guestbook.dto.GuestbookDTO;
import com.yoo.guestbook.dto.PageRequestDTO;
import com.yoo.guestbook.dto.PageResultDTO;
import com.yoo.guestbook.entitiy.Guestbook;
import com.yoo.guestbook.entitiy.QGuestbook;
import com.yoo.guestbook.repository.GuestbookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
        //repository 에서 save 될 시 gno 가 생성된다. 그것을 반환함
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno"));

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 추가

        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); //querydsl 사용
        
        //Entity 객체를 DTO 객체로 변환하는 functional Method 이다
        Function<Guestbook, GuestbookDTO> fn = this::entityToDto;
        return new PageResultDTO<GuestbookDTO, Guestbook> (result,fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        return result.map(this::entityToDto).orElse(null);
    }

    @Override
    public void remove(long gno) {
        log.info("gno ::: " + gno);
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO guestbookDTO) {
        //업데이트 항목 title, content

        log.info("guestbookDTO ::  " +  guestbookDTO);

        Optional<Guestbook> result = repository.findById(guestbookDTO.getGno());
        
        //개시물 유무 확인
        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.setTitle(guestbookDTO.getTitle());
            entity.setContent(guestbookDTO.getContent());
            repository.save(entity);

        }
    }
    
    //////////////////////////////////
    /**
     * @Description  : 동적 검색 조건이 처리되는 경우 실제 코드는 Querydsl 을 통해 
     *                 BooleanBuilder 를 사용하여 작성하고 {@link GuestbookRepository}에서는
     *                 QueryDsl 로 작성된 BooleanBuilder 를 사용하여 findAll()로 처리한다
     *                 
     *                 ✔ BooleanBuilder 에 대한 작성은 별도의 Class를 사용하여 처리할 수있지만
     *                   간단하게 해당 ServiceImpl 에서 Method 를 작성하여 사용함
     * */
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건

        booleanBuilder.and(expression); // gno > 0 조건 and 주입

        if(type == null){ //검색 조건이 없을 경우
            return booleanBuilder;
        }

        //검색 조건이 있을 경우 조건 및 keyword 를 추가!
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.equals("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.equals("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.equals("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }
        if (type.equals("tc")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword))
                    .or(qGuestbook.writer.contains(keyword));
        }
        if (type.equals("tcw")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword))
                    .or(qGuestbook.writer.contains(keyword))
                    .or(qGuestbook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);// 모든 조건을 하나로 합침
        return booleanBuilder;
    }
    //__Eof__
}
