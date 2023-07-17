package com.yoo.batchStudy.job.writer;

import com.yoo.batchStudy.entity.Pay;
import com.yoo.batchStudy.entity.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class CustomItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    @Bean
    public Job customItemWriterJob() {
        return jobBuilderFactory.get("customItemWriterJob")
                .start(customItemWriterStep())
                .build();
    }

    @Bean
    public Step customItemWriterStep() {
        return stepBuilderFactory.get("customItemWriterStep")
                .<Pay, Pay2>chunk(chunkSize)
                .reader(customItemWriterReader())
                .processor(customItemWriterProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> customItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("customItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }

    @Bean
    public ItemProcessor<Pay, Pay2> customItemWriterProcessor() {
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    /**
     * 해당 Witer는 타입이 ItemWriter<T> 이다.
     *
     * 아래의 로직을 커스텀하여 원하는 대로  write 기능을 사용가능하다.
     * */
    @Bean
    public ItemWriter<Pay2> customItemWriter() {
        return items -> {
            // 아래의 로직을 사용하여 Write 로직을 커스텀하여 사용이 가능하다.
            // ✅ Insert 로직이 사용되지 않았음!!!
            for (Pay2 item : items) {
                System.out.println(item);
            }
        };
    }
}
