package com.yoo.batchStudy.tasklet;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
/**
 * 미사용시 Error 발생
 * Error Msg : bean with name 'simpleTasklet': Unsatisfied dependency expressed through field 'requestDate';
 * */
@StepScope
public class SimpleTasklet implements Tasklet {

    @Value("#{jobParameters[requestDate]}")
    private String requestDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("This is Step1 Tasklet Class Call Method");
        log.info("requestDate ::: {}", requestDate);
        return RepeatStatus.FINISHED;
    }
}
