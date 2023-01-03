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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class JPQLTests {

    @Autowired
    private DogRepository dogRepository;

    @Test
    public void getListDescTest(){
        List<Dog> result = dogRepository.getListDesc();

        log.info(Arrays.toString(result.toArray()));

    }

    @Test
    public void updateTest(){
        int result = dogRepository.updateAge(200, 4L);
    }

    @Test
    public void updateTestObjectParamVer(){
        Dog dog = Dog.builder().dnum(4L)
                .age(300)
                .build();
        int result = dogRepository.updateAgeObjectParamVer(dog);
        log.info("Result ::: {}",result);
    }

    @Test
    public void getListWithPageable(){
        Pageable pageable = PageRequest.of(6, 10, Sort.by("dnum").descending());
        Page<Dog> result = dogRepository.getListWithJPQL(40L, pageable);

        log.info("result :: {}", result.getContent());
    }

    @Test
    public void getListWIthPageableAndObjectReturn(){
        Pageable pageable = PageRequest.of(6, 10, Sort.by("dnum").descending());
        Page<Object> result = dogRepository.getListWithJQPLAndObject(40L, pageable);

        log.info("result :: {}", result.getContent());
    }

}
