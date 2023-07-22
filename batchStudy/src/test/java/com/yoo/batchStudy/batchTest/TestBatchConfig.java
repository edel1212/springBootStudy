package com.yoo.batchStudy.batchTest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/***
 * @SpringBatchConfig로 인해 불필요한 설정이 제거된 Config 클래스이다.
 * */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class TestBatchConfig {

}