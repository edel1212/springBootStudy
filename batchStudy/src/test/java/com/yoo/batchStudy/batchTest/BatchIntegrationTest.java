package com.yoo.batchStudy.batchTest;

import com.yoo.batchStudy.job.itemRead.JpaPagingItemReaderJobConfiguration;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SpringBatchTest // Spring Batch 테스트용 어노테이션
// {테스트 대상 Batch, ✅ 필수 설정이 되어있는 BatchConfig(중요!!)}
@SpringBootTest(classes = {JpaPagingItemReaderJobConfiguration.class, TestBatchConfig.class})
@EntityScan("com.yoo.batchStudy.entity")    // 👉 해당 어노테이션이 없으면 Entity를 찾지 못함
public class BatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void batchTestCode() throws Exception {

        // Job Parameter 생성
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("orderDate", LocalDateTime.now().toString())
                .toJobParameters();

        // when
        JobExecution jobExecution =
                jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assert.assertEquals(ExitStatus.COMPLETED,
                jobExecution.getExitStatus());

    }
}