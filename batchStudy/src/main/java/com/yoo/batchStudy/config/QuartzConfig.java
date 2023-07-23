package com.yoo.batchStudy.config;

import com.yoo.batchStudy.job.quartzJob.TestJobA;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .withSchedule(CronScheduleBuilder.cronSchedule("/10 * * * * ?"))
                .build();
    }

}
