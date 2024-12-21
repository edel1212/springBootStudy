package org.zerock.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //AuditingEntityListener 활성화
public class BoardApplication {
	/**
	 * @Descripciton : JPA 에서 다른 테이블과 연관관계를 가지게 하고 싶을때는 FK 기준으로 잡으면 좀 더 편함!
	 * */
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
