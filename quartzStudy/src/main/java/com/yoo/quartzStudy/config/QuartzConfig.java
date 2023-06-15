package com.yoo.quartzStudy.config;

import com.yoo.quartzStudy.job.TestJobA;
import com.yoo.quartzStudy.job.TestJobB;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Properties;
import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
//@RequiredArgsConstructor
public class QuartzConfig {

//    private final ApplicationContext applicationContext;
//
//    @Bean
//    public SpringBeanJobFactory jobFactory() {
//        return new AutowiringSpringBeanJobFactory();
//    }

    @Bean
    public JobDetail jobDetailA() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("count", 0);

        return JobBuilder.newJob(TestJobA.class)
                .storeDurably()
                .withIdentity("testA", "DEFAULT")
                .withDescription("a")
                .usingJobData(jobDataMap)
                .build();
    }

    @Bean
    public Trigger tistoryTrigger(JobDetail jobDetailA) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetailA)
                .withIdentity("test1") // 트리거 식별자 설정
                .withSchedule(cronSchedule("/1 * * * * ?").inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();
    }

//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(JobDetail jobDetailA, Trigger tistoryTrigger) {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        schedulerFactoryBean.setJobFactory(jobFactory());
//        schedulerFactoryBean.setJobDetails(jobDetailA);
//        schedulerFactoryBean.setTriggers(tistoryTrigger);
//
//        Properties quartzProperties = new Properties();
//        quartzProperties.setProperty("org.quartz.scheduler.instanceName", "quartzScheduler");
//        quartzProperties.setProperty("org.quartz.threadPool.threadCount", "5");
//        quartzProperties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
//        schedulerFactoryBean.setQuartzProperties(quartzProperties);
//
//        return schedulerFactoryBean;
//    }
}