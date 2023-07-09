package com.yoo.batchStudy.job;


import com.yoo.batchStudy.tasklet.SimpleTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class JobParamErrorJobConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    //  👉 DI 주입
    private final SimpleTasklet simpleTasklet;

    @Bean
    public Job simpleTaskletJob(){
        return jobBuilderFactory.get("simpleTaskletJob")
                .start(scopeTaskletCallStep1())
                .build();
    }

    @Bean
    public Step scopeTaskletCallStep1(){
        return stepBuilderFactory.get("scopeTaskletCallStep1")
                .tasklet(simpleTasklet).build(); // 👉생성한 Tasklet 사용
    }

}
