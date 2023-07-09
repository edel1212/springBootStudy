package com.yoo.batchStudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class JobParameterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /**
     * 👉 같은 이름의 Bean이 겹치면
     * - Error : could not be registered. A bean with that name has already been defined in class path resource 발생
     * */


    @Bean
    public Job scopeJob(){
        return jobBuilderFactory.get("scopeJob")
                /**
                 * null을 입력해도 Step 단계에서  로그가 찍히는 이유눈
                 * Job Parameter의 할당이 어플리케이션 실행시에 하지 않기 때문에 가능합니다.
                 * */
                .start(scopeStep1( null))
                .build();
    }

    /**
     * 👉 @JobScope가 없을 시 Error 발생
     * - Error :  EL1008E: Property or field 'jobParameters' cannot be found on object of type 에러 발생
     * */
    @StepScope
    @Bean
    public Step scopeStep1(@Value("#{jobParameters[requestDate]}")String requestDate){
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("param ::: {}", requestDate);
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
