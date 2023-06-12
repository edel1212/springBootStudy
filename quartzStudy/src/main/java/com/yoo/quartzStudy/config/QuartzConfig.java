package com.yoo.quartzStudy.config;

import com.yoo.quartzStudy.job.TestJobA;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class QuartzConfig {

    private final JobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        // 스케줄러 설정 및 속성 구성
        // ...
        return schedulerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean sampleJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(TestJobA.class);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean sampleJobTrigger(JobDetail jobDetail) {
        SimpleTriggerFactoryBean triggerFactory = new SimpleTriggerFactoryBean();
        triggerFactory.setJobDetail(jobDetail);
        triggerFactory.setRepeatInterval(5000); // 5초마다 반복
        return triggerFactory;
    }

}
