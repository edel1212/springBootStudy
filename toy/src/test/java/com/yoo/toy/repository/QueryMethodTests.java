package com.yoo.toy.repository;

import com.yoo.toy.entity.Dog;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class QueryMethodTests {

    @Autowired
    private DogRepository dogRepository;

    @Test
    public void testD(){

        List<Dog> a = dogRepository.findAll();

        log.info(Arrays.toString(a.toArray()));

    }

    @Test
    public void insertTest(){
        List<Long> indexArr = new ArrayList<>();
        IntStream.rangeClosed(0, 100).forEach(i ->{
            Dog dogEntity = Dog.builder()
                    .age(10)
                    .name("흑곰!!")
                    .build();
            dogRepository.save(dogEntity);
            indexArr.add(dogEntity.getDnum());
        });

        log.info(indexArr.toArray());

    }


    @Test
    public void readTest(){
        Optional<Dog> result =  dogRepository.findById(99999L);

        //getOne의 경우 Deprecated 되어있음 더는 사용하지 않음을 권장함!
        //dogRepository.getOne();

        result.ifPresent(log::info);

    }

    @Test
    public void updateTest(){

        Dog dog = Dog.builder()
                .dnum(1L)
                .age(1)
                .name("아가 흑곰")
                .build();

        log.info(dogRepository.save(dog));

    }


    @Test
    public void deleteTest(){

        dogRepository.deleteById(2L);

    }


    ////////////////////////////////////////////////////////////

    //Paging
    @Test
    public void testPageDefault(){
        
        //1 ~ 10 번 페이지
        Pageable pageable = PageRequest.of(0, 10); //PageRequest . of() 를 통해 객체를 생성해야함 new 불가능!

        Page<Dog> result = dogRepository.findAll(pageable);

        log.info(result.toList());
    }


    @Test
    public void testPagingWithSort(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("dnum").descending() // dnum Desc
                .and(Sort.by("age").ascending()));                                     // age Asc

        Page<Dog> result = dogRepository.findAll(pageable);

        log.info(result.getContent());
    }

    @Test
    public void tt(){
        log.info("???");
        log.info((int)(Math.random()*45)+1);
    }

    @Test
    public void ageUpdateTest(){
        List<Dog> dogs = dogRepository.findAll();
        dogs.forEach(data->{
            dogRepository.save(
                Dog.builder().dnum(data.getDnum()).age((int)(Math.random()*45)+1)
                        .name(data.getName())
                        .build()
            );
        });
    }


    //////////////////////////////
    //QueryMethod
    @Test
    public void queryMethodTest(){
        log.info(dogRepository.findByAge(1));
        log.info(dogRepository.findBydnum(20L));
        log.info(dogRepository.findByName("흑곰!!"));
    }

    @Test
    public void queryMethodFirstAndLast(){

        log.info(dogRepository.findFirst2ByAge(10)); //첫 2개

        log.info(dogRepository.findLast1ByAge(10)); //마지막 1개
        
        //log.info(dogRepository.findLast2ByAge(10)); //마지막 2개
    }


    @Test
    public void greaterThanAndLessThanTest(){
        log.info("----------------");
        log.info("20살 초과 개수 :: " +dogRepository.findByAgeGreaterThan(20).size());
        log.info("----------------");
        log.info("20살 이상 개수 :: " +dogRepository.findByAgeGreaterThanEqual(20).size());
        log.info("----------------");
        log.info("20살 이상 미만 :: " +dogRepository.findByAgeLessThan(20).size());
        log.info("----------------");
        log.info("20살 이상 이하 :: " +dogRepository.findByAgeLessThanEqual(20).size());
        log.info("----------------");
    }


    @Test
    public void isNullOrIsEmptyTest(){
        log.info("----------------------------------");
        log.info(dogRepository.findByAgeIsNotNull().size());
        log.info("----------------------------------");
        log.info(dogRepository.findByAgeIsNull().size());
        log.info("----------------------------------");
    }
    
    @Test
    public void queryInNameTest(){
        log.info(dogRepository.findByNameIn(List.of("흑곰")));
    }

    @Test
    public void likeMethodTest(){
        log.info("----------------------------");
        dogRepository.findByNameStartingWith("흑");
        log.info("----------------------------");
        dogRepository.findByNameEndingWith("!");
        log.info("----------------------------");
        dogRepository.findByNameContains("흑");
        log.info("----------------------------");
        dogRepository.findByNameLike("흑");
        log.info("----------------------------");
    }

    @Test
    public void sortTest(){
        log.info("-----");
        log.info(dogRepository.findByNameOrderByDnumDesc("흑곰!!"));
        log.info("-----");
    }


    ///////////////////////////////////////
    //QueryMethod With Page
    @Test
    public void queryMethodWithPage(){
        Long startDnum = 10L;
        Long endDnum   = 50L;
        Pageable pageable = PageRequest.of(0, 10 , Sort.by("dnum").descending());
        Page<Dog> result = dogRepository.findByDnumBetween(startDnum, endDnum, pageable);

        log.info("--------------------");
        log.info(result.getContent());
        log.info("--------------------");

    }

    //Delete QueryMethod
    /**
     * @Description : Error -  No EntityManager with actual transaction available for current thread
     *                         - cannot reliably process 'remove' call
     * */
    @Test
    @Transactional // 없으면 Error
    @Commit        // 없으면 Error는 없지만 DB 반영 X
    public void deleteWithQueryMethodTests(){
        dogRepository.deleteByDnumLessThan(4); //4 미만 삭제
    }




}
