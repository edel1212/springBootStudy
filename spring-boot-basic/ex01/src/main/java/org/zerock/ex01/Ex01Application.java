package org.zerock.ex01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex01Application {
	/**
	 * @Description : gradle 서버를 기동하는 곳 단축키 Shift + F10
	 *                🎈 내가 run 할 class 를 정해서 하는 방법 Alt + Shift + F10
	 *
	 *                🎈 gradle 빌드 단축키 Ctrl + F9
	 * */
	public static void main(String[] args) {
		SpringApplication.run(Ex01Application.class, args);
	}

}
