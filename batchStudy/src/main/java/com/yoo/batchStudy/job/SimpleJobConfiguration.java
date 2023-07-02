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
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    /***
     * !주의 사항!
     *  - 일반적은 simpleJob(), simpleStep1() 으로 메서드 생성 후
     *    Bean 주입 시 Bean 이름이 중복되는 에러가발생함!!
     *
     *  - H2DB가 아닌 Database 사용 시 Batch에 필요한 메타 테이블을 꼭 추가해줘야한다!
     *    안해줄 시 'BATCH_JOB_INSTANCE' 에러 발생 >> `schema-사용DB.sql` 파일에 있음
     *
     *
     *  - 메타 테이블 설명
     *      - BATCH_JOB_INSTANCE : 실행 되었던 Job이 기록되어있으며 Job Parameter에 맞춰 추가된다
     *
     * **/

    @Bean
    public Job simpleJob() {
        // Job 생성
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1(){
        // Step 생성
        return stepBuilderFactory.get("simpleStep1")
                // Step 단위 내에서 실행될 커스텀 기능
                .tasklet((contribution, chunkContext)->{
                    log.info(">>>> THis is Step1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
