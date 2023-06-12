package com.yoo.quartzStudy.job;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Log4j2
public class TestJobB implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("@@@@@");
    }
}
