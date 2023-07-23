package com.yoo.batchStudy.config;

import com.yoo.batchStudy.job.quartzJob.TestJobA;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Log4j2
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetailA() {

        return JobBuilder.newJob(TestJobA.class)
                .storeDurably()
                .withIdentity("jobDetailA")
                .withDescription("jobDetailA Descriptin")
                .build();
    }

    @Bean
    public Trigger tistoryTrigger(JobDetail jobDetailA) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetailA)
                .withIdentity("test1") // 트리거 식별자 설정
                .withSchedule(CronScheduleBuilder.cronSchedule("/1 * * * * ?").inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();
    }

}
