package com.yoo.batchStudy.job;

import com.yoo.batchStudy.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class JdbcCursorItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;        // DataSource 주입

    private static final int CHUNK_SIZE = 10;   // Chunk Size 설정

    /**
     * 👉 JdbcItemReader는 JdbcTemplate 과 인터페이스가 동일하기 사용 문법이 비슷하다.
     * 👉 Jpa에는 CursorItemReader가 없다
     * 👉 CursorItemReader를 사용하실때는 Database와 SocketTimeout을 충분히 큰 값으로 설정해야만 합니다.
     * 👉 따라서  Batch 수행 시간이 오래 걸리는 경우에는 PagingItemReader를 사용하시는게 안전하고 좋다
     *    - Paging의 경우 한 페이지를 읽을때마다 Connection을 맺고 끊기 때문에 많은 데이터라도 타임아웃과 부하 없이 수행이 가능함
     * */

    @Bean
    public Job jdbcCursorItemReaderJob(){
        return jobBuilderFactory.get("jdbcCursorItemReaderJob")
                .start(jdbcCursorItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep(){
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                /**
                 * 👉 제네릭 설정을 해주지 않으면 Writer 부분에서 Error 발생
                 * <Pay, Pay>
                 *     첫번쨰로 선언된 Pay는 reader()에서 "반환"될 타입이며
                 *     두번째로 선언된 Pay는 writer()에 "파라미터"로 넘어올 타입이다.
                 *
                 * (CHUNK_SIZE)
                 *      Reader & Writer가 묶일 Chunk 트랜잭션 범위입니다
                 * */
                .<Pay, Pay>chunk(CHUNK_SIZE)
                .reader(jdbcCursorItemReader())
                //.processor() 👉 비즈니스 로직이 필요한 경우 사용하고 필수가 아니기에 사용하지 않으면 쓰지 않아도 된다.
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> jdbcCursorItemReader(){
        return new JdbcCursorItemReaderBuilder<Pay>()
                /**
                 *  - Database에서 한번에 가져올 데이터 양을 설정
                 *  - 내부적으로 가져오는 데이터를 해당 설정만큼 가져와 read()를 통해 하나씩 가져와 사용
                 * */
                .fetchSize(CHUNK_SIZE)
                /**
                 * Data Source 설정
                 * */
                .dataSource(dataSource)
                /**
                 * - 쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper 입니다.
                 * - 커스텀하게 생성해서 사용할 수 도 있지만, 이렇게 될 경우 매번 Mapper 클래스를 생성해야 되기에
                 *   보편적으로는 Spring에서 공식적으로 지원하는 BeanPropertyRowMapper.class를 많이 사용함
                 * */
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
                /**
                 * - reader의 이름을 지정합니다.
                 * - Spring Batch의 ExecutionContext에서 저장되어질 이름입니다.
                 * */
                .name("jdbcCursorItemReader") // 이름 설정
                .build();
    }

    private ItemWriter<Pay> jdbcCursorItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }

}
