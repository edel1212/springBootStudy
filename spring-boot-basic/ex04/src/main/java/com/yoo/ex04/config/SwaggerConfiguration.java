package com.yoo.ex04.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration  // scan 대상에 추가
public class SwaggerConfiguration {

    private static final String API_NAME = "Programmers Spring Boot Application - yoo";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Swagger!";

    @Bean // Bean 등록
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())  // 설정정보를 Parameter로 추가[ ApiInfo Type ]
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yoo.ex04")) // Swagger API를 생성할 BasePackage 범위 지정
                .paths(PathSelectors.any()) // apis 에 위치하는 API 중 특정 path 를 선택
                .build();
    }

    /***
     * @Description : Swagger Setting info
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Rest API Swagger - yoo")
                .description("Swagger!!!")
                .version("1.0")
                .build();
    }

}
