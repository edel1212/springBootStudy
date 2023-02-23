<h1>Spring Security, 소셜 로그인, JWT</h1>

<h3>1 ) Spring Security </h3>

1. 사용을 위한 dependencies 추가 🔽

```groovy
//build.gradle

//...code ... 

dependencies {

    //...code ...
    
    //Security 추가
    implementation 'org.springframework.boot:spring-boot-starter-security'
    /*현재  springsecurity6 버전에는 문제가 있어서 5버전으로 버전을 낮춘 후 개발 진행*/
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
    testImplementation 'org.springframework.security:spring-security-test'

    //...code ...
    
}

//...code ...


```

<br/>

2 . Logging Level 설정  🔽
```properties
#application.properties

############################
##Setting Security Log Level
############################
logging.level.org.springframework.security.web= trace
```

<br/>

3 . 💬 적용 후 서버를 가동하면 Log에 **Pw가 적혀있고** 여기서 가장 중요한것은 따로 Controller , View를 만들지 않았지만  
***localhost:port***에 접근시 로그인 화면과 ID:user PW:로그에 남겨있는 값을 작성 시 **로그인이 되는것을 확인할 수 있다.**

<br/>

4 . Spring Security 흐름 및 설정 방법 **[ Class 사용 ]** 
 - 앞에 테스트 했던거와 같이 스프링 부트에서는 자동 설정 기능이 있기에 Spring과 다르게 따로 web.xml의 별도 설정이 없이도  
사용이 가능하지만 ***조금 더 세부적인 설정과 프로젝트에 맞는 설정을 하기 위해서는 따로 설정파일을 추가 해줘야한다.***
 - Spring Security 설정은 따로 설정 Class를 구분하여 사용하는것이 일반적이다.


- 💬 문제사항 : Spring의 버전 업데이트로 인해 이전 버전에는 WebSecurityConfigurerAdapter를 상속 받아   
  **@Override 메서드인 configure()** 을 구현하여 설정하였지만 **Deprecated** 되었기에 새롭게 변경된 방식 
            해당 Class 에 보안문제가 있어어 Deprecated 되어 사용할수 없게 되었다.  
👉 따라서 ***SecurityFilterChain*** 를 구현하여 Bean에 등록하는 방식을 사용함.


- Spring Security의 간단한 흐름
  -  1단계 : 어떤 기능을 요청 받으면 핵심 역할을 하는 Authentication Manager(인증 매니저)를 통해 이뤄진다.
  -  2단계 : Authentication Provider는 인증 매니저가 어떻게 해야하는지를 결정하고
  -  3단계 : 최종적인 인증은 userDetailsService에서 이뤄진다.

\- Security Config Class  🔽
```java
//java - Security Config 

```