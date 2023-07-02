package com.yoo.batchStudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing // batch를 사용하기 위해 필 수 선언
public class BatchStudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BatchStudyApplication.class, args);
	}
}
