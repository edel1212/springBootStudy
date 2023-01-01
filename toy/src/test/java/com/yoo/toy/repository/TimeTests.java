package com.yoo.toy.repository;

import com.yoo.toy.entity.Dog;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TimeTests {

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




}
