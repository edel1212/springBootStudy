package com.yoo.quartzStudy.config;

import com.yoo.quartzStudy.job.TestJobA;
import com.yoo.quartzStudy.job.TestJobB;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class QuartzConfig {

    /**
     * 따라서 간단한 스케줄링 작업이 필요하고 Spring Framework를 사용하고 있다면 @Scheduled를 사용할 수 있습니다.
     * 그러나 복잡한 스케줄링 작업이나 분산 환경에서 작업을 관리해야 하는 경우 Quartz를 사용하는 것이 더 적합할 수 있습니다.
     *
     * -https://advenoh.tistory.com/51
     * */

    @Bean
    public JobDetail jobDetailA() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("count",0);

        return JobBuilder.newJob().ofType(TestJobA.class)
                .storeDurably()
                .withIdentity("testA")
                .withDescription("a")
                .setJobData(jobDataMap)
                .build();
    }

    @Bean
    public Trigger tistoryTrigger(JobDetail jobDetailA) {
        return TriggerBuilder.newTrigger().forJob(jobDetailA)
                // 트리거의 이름을 설정합니다. 이 이름은 트리거를 식별하는 데 사용됩니다.
                // 같은 이름의 트리거를 여러 개 생성할 경우, 마지막에 생성된 트리거가 유지됩니다.
                .withIdentity("test1")
                .withSchedule(cronSchedule("/1 * * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
                .build();
    }


}
