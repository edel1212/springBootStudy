package com.yoo.multipleDB.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
// JPA Repository Bean 활성화
@EnableJpaRepositories(
        // JPA Repository가 위치한 패키지 지정
        basePackages = "com.yoo.multipleDB.repository.primary",
        // 해당 데이터 소스에 대한 엔티티 매니저 팩토리 지정
        entityManagerFactoryRef = "primaryEntityManager",
        // 트랜잭션 매니저를 지정하여 JPA의 트랜잭션을 관리
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryConfig {

    @Bean
    @Primary
    // application properties 값을 읽어옴
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        // DataSourceBuilder를 사용하여 DataSource를 생성
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryEntityManager")
    public LocalContainerEntityManagerFactoryBean primaryEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                // Entity 클래스가 위치한 패키지를 지정
                .packages("com.yoo.multipleDB.entity.primary")
                // 해당 데이터 소스의 persistence unit 이름 지정
                .persistenceUnit("primarydb")
                .build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManager") EntityManagerFactory entityManagerFactory) {
        //  JPA 트랜잭션을 관리하는 Spring의 트랜잭션 매니저, EntityManagerFactory를 사용하여 트랜잭션을 관리
        return new JpaTransactionManager(entityManagerFactory);
    }
}
