package com.yoo.exGraphQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //AuditingEntityListener 활성화
public class ExGraphQlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExGraphQlApplication.class, args);
	}

}
