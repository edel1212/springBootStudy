package com.yoo.batchStudy.job.itemRead;

import com.yoo.batchStudy.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Log4j2
@Configuration
public class JpaPagingItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    // 👉 JpaPagingItemReaderBuilder 사용을 위함
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean("jpaPagingItemReaderJob")
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> jpaPagingItemReader(){
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)                 // entityManagerFactory 주입
                .pageSize(chunkSize)                                        // 페이징 개수
                .queryString("SELECT p FROM Pay p WHERE amount >= 2000 ORDER BY p.id DESC")    // JPA Query 방식
                .build();
        /**
         * Result Query
         * Hibernate:
         *     select
         *         pay0_.id as id1_0_,
         *         pay0_.amount as amount2_0_,
         *         pay0_.tx_date_time as tx_date_3_0_,
         *         pay0_.tx_name as tx_name4_0_
         *     from
         *         pay pay0_
         *     where
         *         pay0_.amount>=2000
         *     order by
         *         pay0_.id DESC limit ?
         * */
    }

    private ItemWriter<Pay> jpaPagingItemWriter() {
        return list -> list.stream().forEach(log::info);
    }

}
