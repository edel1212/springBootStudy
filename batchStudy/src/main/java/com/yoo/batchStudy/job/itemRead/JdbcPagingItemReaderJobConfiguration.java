package com.yoo.batchStudy.job.itemRead;

import com.yoo.batchStudy.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * JdbcPagingItemRedaer는 JdbcCursorItemReader와 같은
 * JdbcTemplate 인터페이스를 이용한 PagingItemReader입니다.
 * */
@Log4j2
@Configuration
@RequiredArgsConstructor
public class JdbcPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .start(jdbcPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Pay> jdbcPagingItemReader() throws Exception {
        // 파라미터 정의
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("amount", 2000);

        return new JdbcPagingItemReaderBuilder<Pay>()
                .pageSize(chunkSize)                // 페이징 사이즈 절성
                .fetchSize(chunkSize)               // Database에서 한번에 가져올 데이터 양을 설정
                .dataSource(dataSource)
                /**
                 * - 쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper 입니다.
                 * - 커스텀하게 생성해서 사용할 수 도 있지만, 이렇게 될 경우 매번 Mapper 클래스를 생성해야 되기에
                 *   보편적으로는 Spring에서 공식적으로 지원하는 BeanPropertyRowMapper.class를 많이 사용함
                 * */
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .queryProvider(createQueryProvider()) // 쿼리 주입
                .parameterValues(parameterValues)     // 쿼리에 사용 될 파라미터
                .name("jdbcPagingItemReader")
                .build();
    }

    // 필요 쿼리 생성
    /**
     * ReadItem의 필요 쿼리 생성정
     *
     * 💬 SqlPagingQueryProviderFactoryBean 생성 이유
     *      각 Database에는 Paging을 지원하는 자체적인 전략들이 있습니다.
     *      때문에 Spring Batch에는 각 Database의 Paging 전략에 맞춰 구현되어야만 합니다.
     *      그래서 아래와 같이 각 Database에 맞는 Provider들이 존재하는데 그것을
     *      Datasource 설정값을 보고 위 이미지에서 작성된 Provider중 하나를 <b>자동으로<b/> 선택하도록 합니다.
     * */
    @Bean
    public PagingQueryProvider createQueryProvider()throws Exception{
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);

        // 필요 쿼리
        queryProviderFactoryBean.setSelectClause("id, amount, tx_name, tx_date_time");
        queryProviderFactoryBean.setFromClause("from pay");
        queryProviderFactoryBean.setWhereClause("where amount >= :amount");

        // 정렬 조건
        Map<String, Order>  sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProviderFactoryBean.setSortKeys(sortKeys);

        return queryProviderFactoryBean.getObject();
    }

    // Write
    private ItemWriter<Pay> jdbcPagingItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }

}
