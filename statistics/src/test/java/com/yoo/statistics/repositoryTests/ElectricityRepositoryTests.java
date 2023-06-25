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
    @Description("하루 누적량")
    public void testCode2(){
        repository.getElectricityOfToday();
    }

    @Test
    @Description("각각 시간별 누적양")
    public void testCode3(){
        repository.getElectricityByHourOfDay();
    }

    @Test
    @Description("한달 누적양")
    public void testCode4(){
        repository.getElectricityOfMonth();
    }

    @Test
    @Description("1년 누적양")
    public void testCode5(){
        repository.getElectricityOfYear();
    }

}
