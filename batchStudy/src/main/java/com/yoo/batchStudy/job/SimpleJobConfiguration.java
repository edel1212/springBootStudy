package com.yoo.batchStudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    /***
     * !주의 사항!
     *  - H2DB가 아닌 Database 사용 시 Batch에 필요한 메타 테이블을 꼭 추가해줘야한다!
     *    안해줄 시 'BATCH_JOB_INSTANCE' 에러 발생 >> `schema-사용DB.sql` 파일에 있음
     * **/

    @Bean
    public Job simpleJob() {
        // Job 생성
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext)->{
                    log.info(">>>> THis is Step1");
                    log.info(">>>>>>>>>>>>> requestDate = {}",requestDate);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){

        // Step 생성
        return stepBuilderFactory.get("simpleStep2")
                // Step 단위 내에서 실행될 커스텀 기능
                .tasklet((contribution, chunkContext)->{
                    log.info(">>>> THis is Step2");
                    log.info(">>>>>>>>>>>>> requestDate = {}",requestDate);
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
