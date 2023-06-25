package com.yoo.statistics.repositoryTests;

import com.yoo.statistics.entity.Electricity;
import com.yoo.statistics.repository.ElectricityRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class ElectricityRepositoryTests {

    @Autowired
    private ElectricityRepository  repository;

    @Test
    @Description("DummyInsert")
    public void testCode1(){

        IntStream.rangeClosed(1, 1000).forEach(item ->{
            Electricity electricity = null;

            if( item%2 == 0){
                electricity = Electricity.builder()
                        .value(Double.valueOf(new Random().nextInt(100)))
                        .regDate(LocalDateTime.now().minusHours(item))
                        .build();
            } else{
                electricity = Electricity.builder()
                        .value(Double.valueOf(new Random().nextInt(100)))
                        .regDate(LocalDateTime.now().minusDays(item))
                        .build();
            }// if - else
            repository.save(electricity);
        });
    }


    @Test
    public void testCode2(){
        repository.statistics();
    }

}
