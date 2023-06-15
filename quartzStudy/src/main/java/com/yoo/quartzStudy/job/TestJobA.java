package com.yoo.quartzStudy.job;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class TestJobA implements Job {

    private static int cnt  = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        //int count = jobDataMap.getInt("count");
//        log.info("count before: {}", count);
//        count++;
//        jobDataMap.put("count", count);
//        log.info("count after: {}", count);

        log.info(cnt);

        cnt++;

    }
}
