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
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class JpaItemWriterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    // spring-boot-starter-data-jpa를 의존성에 등록하면 Entity Manager가 Bean으로 자동생성 된다.
    private final EntityManagerFactory  entityManagerFactory;

   private static final int chunkSize = 10;


    @Bean
    public Job jpaItemWriterJob() {
        return jobBuilderFactory.get("jpaItemWriterJob")
                .start(jpaItemWriterStep())
                .build();
    }

    @Bean
    public Step jpaItemWriterStep() {
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<Pay, Pay2>chunk(chunkSize)    // Chunk Size 지정
                .reader(jpaItemWriterReader())
                .processor(jpaItemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    /**
     * Item Read 역할 수행
     * */
    @Bean
    public JpaPagingItemReader<Pay> jpaItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }

   /**
   * Reader에서 읽은 데이터를 Writer에서 사용 할 수 있도록 가공 해주는 역할을 해줌
    *
    * 제네릭 설명 <ReadItem, WriteItem>
   * */
   @Bean
   public ItemProcessor<Pay, Pay2> jpaItemProcessor(){
       return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
       //return pay -> new Pay2(19930223L, pay.getTxName(), pay.getTxDateTime()); 👉 변경한 값이 Write 되는 것 확인 완료
   }

    /**
     * JpaItemWriter 는 넘어온 Item을 그대로 entityManger.merge()로 테이블에 반영한다.
     *
     * 👉 포인트는 알아서 Processor를 거쳐서 변환 된 값을 자동으로 Merge를 진행 했다는 것이다.
     * */
   @Bean
    public JpaItemWriter<Pay2> jpaItemWriter(){
        JpaItemWriter<Pay2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
