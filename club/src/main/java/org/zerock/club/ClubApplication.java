package org.zerock.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClubApplication {
	/**
	 * 서버 실행 시 Spring security 가 추가된 후 console 창에 임시 패스워드가 생성된것을 확인 가능함
	 *  - Using generated security password: ~~~~~
	 *
	 *  - 해당 패스워드는 spring security 에서 자동으로 제공하는 화면인 : localhost:8080 에 접속 시
	 *    로그인을 할수 있는 PW 이다 - id :: <b>user</b>
	 * */
	public static void main(String[] args) {
		SpringApplication.run(ClubApplication.class, args);
	}

}
