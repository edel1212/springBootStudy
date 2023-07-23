package com.yoo.batchStudy.job.quartzJob;

import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TestJobA extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
}
