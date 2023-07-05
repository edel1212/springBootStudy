package com.yoo.batchStudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 예상 시나리오
     * 1 ) Step1 실패 시나리오: step1 -> step3
     * 2 ) Step1 성공 시나리오: step1 -> step2 -> step3
     * */

    @Bean
    public Job stepNextConditionalJob(){
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())   // ✅ conditionalJobStep1()을 실행
                    .on("FAILED")                // 👉 실패할 경우 - IF 문으로 보자
                    .to(conditionalJobStep3())          // 👉 conditionalJobStep3()을 실행
                    .on("*")                    // 👉 conditionalJobStep3()의 결과는 상관없이
                    .end()                             // Job 종료
                .from(conditionalJobStep1())    // ✅ conditionalJobStep1()으로 부터 체이닝을 위해 재선언 (싫행은 X)
                    .on("*")                    // 👉 FAILED 외에 모든 경우
                    .to(conditionalJobStep2())          // 👉 step2로 이동한다.
                    .next(conditionalJobStep3())        // 👉 step2가 정상 종료되면 step3으로 이동한다.
                    .on("*")                    // 👉 step3의 결과 관계 없이
                    .end()                             // 👉 step3으로 이동하면 Flow가 종료한다.
                .end()                          // ✅ Job 종료
                .build();
    }

    @Bean
    public Step conditionalJobStep1(){
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");
                    // 👉 일부러 상태를 "FAILED"로 만듬
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
