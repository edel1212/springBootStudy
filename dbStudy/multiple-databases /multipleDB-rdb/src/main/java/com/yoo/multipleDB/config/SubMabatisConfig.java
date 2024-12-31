package com.yoo.multipleDB.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.yoo.multipleDB.repository.mapper", sqlSessionFactoryRef = "subMybatisSqlSessionFactory")
public class SubMabatisConfig {

    @Bean(name = "subMybatisDataSource")
    @ConfigurationProperties(prefix = "spring.sub-db2")  // 'sub-db2' 데이터소스를 사용
    public DataSource subMybatisDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "subMybatisSqlSessionFactory")
    public SqlSessionFactory subMybatisSqlSessionFactory(@Qualifier("subMybatisDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // MyBatis XML 파일 경로 설정
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml")
        );

        return factoryBean.getObject();
    }

    @Bean(name = "subMybatisTransactionManager")
    public DataSourceTransactionManager subMybatisTransactionManager(@Qualifier("subMybatisDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}