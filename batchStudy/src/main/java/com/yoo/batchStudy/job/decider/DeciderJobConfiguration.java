package com.yoo.batchStudy.job.decider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJbo(){
        return jobBuilderFactory.get("deciderJob")  // Job Name
                .start(startStep())                 // Start Step
                .next(decider())                    // Decider 주입
                    .on("ODD")               // Decider에서 커스텀한 값
                    .to(oddStep())                  // 일치할 경우 실행
                .from(decider())                    //  EventListener 재등록
                    .on("EVEN")              // Decider에서 커스텀한 값
                    .to(evenStep())                 // 일치할 경우 실행
                .end()
                .build();
    }

    @Bean
    public Step startStep(){
        return stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Start!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    // 짝수
    @Bean
    public Step evenStep(){
        return stepBuilderFactory.get("evenStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("짝수 입니다!!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    // 홀수
    @Bean
    public Step oddStep(){
        return stepBuilderFactory.get("oddStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("홀수 입니다!!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    // Bean Factory 등록 👉 중요
    @Bean
    public JobExecutionDecider decider(){
        return new OddDecider();
    }

    // JobExecutionDecider를 구현한 Inner class
    public static class OddDecider implements JobExecutionDecider{
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            int randomNum = new Random().nextInt(50);
            log.info("randomNum ::: {}",randomNum);
            String result = randomNum % 2 == 0 ? "EVEN" : "ODD";
            return new FlowExecutionStatus(result);
        }
    }

}