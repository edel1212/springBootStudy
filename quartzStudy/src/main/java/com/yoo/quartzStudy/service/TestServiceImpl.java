package com.yoo.quartzStudy.service;

import com.yoo.quartzStudy.job.TestJobA;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TestServiceImpl implements TestService{



    public void test() throws SchedulerException{
        // Job 생성
        JobDetail job = JobBuilder.newJob(TestJobA.class)
                .withIdentity("myJob", "group1")
                .build();

        // Trigger 생성
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        // 스케줄러 생성 및 Job, Trigger 등록
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

}
