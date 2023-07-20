package com.yoo.batchStudy.batchTest;

import com.yoo.batchStudy.job.transaction.TransactionProcessorJobConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes={TransactionProcessorJobConfiguration.class})
public class BatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Test
    void test() {
        System.out.println("!!!");
    }
}
