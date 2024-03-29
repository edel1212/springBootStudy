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
@Configuration
@RequiredArgsConstructor
public class StepNextJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // --job.name=stepNextJob version=2 로 실행

    @Bean
    public Job stepNextJob(){
        // application.properties에서 설정한 Job Name을 찾음
        return jobBuilderFactory.get("stepNextJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> THis is Step1");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> THis is Step2");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> THis is Step3");
                    return RepeatStatus.FINISHED;
                })).build();
    }

}
