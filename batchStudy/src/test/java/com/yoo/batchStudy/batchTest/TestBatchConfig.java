package com.yoo.batchStudy.batchTest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/***
 * @SpringBatchConfig로 인해 불필요한 설정이 제거된 Config 클래스이다.
 * */
@Configuration
@EnableAutoConfiguration    // Spring Application Context의 자동 구성을 활성화하여 필요할 수 있는 Bean을 등록
@EnableBatchProcessing      // Spring Batch 기능을 활성화하고 @Configuration 클래스에서 배치 작업을 설정하기 위한 기본 구성 제공
public class TestBatchConfig {

}