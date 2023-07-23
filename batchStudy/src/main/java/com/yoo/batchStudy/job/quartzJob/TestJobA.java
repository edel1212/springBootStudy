package com.yoo.batchStudy.job.quartzJob;

import com.yoo.batchStudy.job.itemRead.JpaPagingItemReaderJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
@RequiredArgsConstructor
@EntityScan("com.yoo.batchStudy.entity") // 👉 Entity를 찾지 못하는 문제로 추가
public class TestJobA extends QuartzJobBean {
    private final JobLauncher jobLauncher;

    private final Job jpaPagingItemReaderJob;// 👉  실행 시킬 Job의 Bean Name이다

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // Job Parameter 생성
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("orderDate", LocalDateTime.now().toString())
                .toJobParameters();
        try {
            // 실행
            jobLauncher.run(jpaPagingItemReaderJob,  jobParameters);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
