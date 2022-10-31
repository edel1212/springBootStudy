package org.zerock.mreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //@EntityListeners(value = {AuditingEntityListener.class}) 를 사용하기 위함
public class MreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MreviewApplication.class, args);
	}

}
