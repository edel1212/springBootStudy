package com.yoo.batchStudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class JobParameterStepScopeConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job stepScopeJob(){
        return jobBuilderFactory.get("job1Test")
                .start(stepFoo1())
                .build();
    }

    @Bean
    public Step stepFoo1(){
        return stepBuilderFactory.get("stepFoo1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(simpleWriterReader());
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    @StepScope
    public ListItemReader<Integer> simpleWriterReader(){
        List<Integer> item = IntStream.rangeClosed(1,50).boxed().collect(Collectors.toList());
        return new ListItemReader<>(item);
    }


}
