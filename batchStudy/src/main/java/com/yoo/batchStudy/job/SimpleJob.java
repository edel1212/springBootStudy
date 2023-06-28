package com.yoo.batchStudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class SimpleJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customSimpleJob() {
        // Job 생성
        return jobBuilderFactory.get("job1")
                .start(customSimpleStep1())
                .build();
    }


    @Bean
    public Step customSimpleStep1(){
        // Step 생성
        return stepBuilderFactory.get("step1")
                // Step 단위 내에서 실행될 커스텀 기능
                .tasklet((contribution, chunkContext)->{
                    log.info(">>>> THis is Step1");
                    return RepeatStatus.FINISHED;
                }).build();
    }




}
