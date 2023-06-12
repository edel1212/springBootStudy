package com.yoo.quartzStudy.config;

import com.yoo.quartzStudy.job.TestJobA;
import com.yoo.quartzStudy.job.TestJobB;
import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetailA() {
        return JobBuilder.newJob().ofType(TestJobA.class)
                .storeDurably()
                .withIdentity("testA")
                .withDescription("a")
                .build();
    }

    @Bean
    public Trigger tistoryTrigger(@Qualifier("jobDetailA") JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("tistory_job_trigger")
                .withSchedule(cronSchedule("/5 * * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();
    }

    @Bean
    public JobDetail jobDetailB() {
        return JobBuilder.newJob().ofType(TestJobB.class)
                .storeDurably()
                .withIdentity("testB")
                .withDescription("b")
                .build();
    }

    @Bean
    public Trigger tistoryTrigger2(@Qualifier("jobDetailB") JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("test2")
                .withSchedule(cronSchedule("/5 * * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();
    }

}
