package com.yoo.batchStudy.job.processor;

import com.yoo.batchStudy.entity.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class ProcessorConvertJobConfiguration {

    public static final String JOB_NAME = "ProcessorConvertBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()   // 실패한 Job 재실행 방지
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "step")
    @JobScope
    public Step step() {
        return stepBuilderFactory.get(BEAN_PREFIX + "step")
                .<Teacher, String>chunk(chunkSize)  // <Reader대상, Writer 대상> 이다
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Teacher> reader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .name(BEAN_PREFIX+"reader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }

    /**
     * 👉 해당 제네릭에서 전달 값의 의미는
     *    <Reader대상, Writer 대상> 이다
     * */
    @Bean
    public ItemProcessor<Teacher, String> processor() {
        // Processor에서 Name만을 정제해서 전달함
        return teacher -> teacher.getName();
    }

    // Processor에서 넘어온 Item을 Write
    private ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                log.info("Teacher Name={}", item);
            }
        };
    }
}
