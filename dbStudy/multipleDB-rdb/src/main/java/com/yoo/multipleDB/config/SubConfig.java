package com.yoo.multipleDB.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
// JPA Repository Bean 활성화
@EnableJpaRepositories(
        basePackages = "com.yoo.multipleDB.repository.sub",
        entityManagerFactoryRef = "subEntityManager",
        transactionManagerRef = "subTransactionManager"
)
public class SubConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.sub-db") // 두 번째 데이터 소스에 대한 설정
    public DataSource subDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "subEntityManager")
    public LocalContainerEntityManagerFactoryBean subEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("subDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.yoo.multipleDB.entity.sub") // Sub 데이터베이스 엔티티 경로
                .persistenceUnit("subdb")
                .build();
    }

    @Bean(name = "subTransactionManager")
    public PlatformTransactionManager subTransactionManager(
            @Qualifier("subEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
