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


- 💬 Spring Security의 간단한 흐름
  -  1단계 : 어떤 기능을 요청 받으면 핵심 역할을 하는 Authentication Manager(인증 매니저)를 통해 이뤄진다.
  -  2단계 : Authentication Provider는 인증 매니저가 어떻게 해야하는지를 결정하고
  -  3단계 : 최종적인 인증은 userDetailsService에서 이뤄진다.

- 💬 Spring Security의 필터와 필터 체이닝
  - Bean과 연동 할 수 있는 구조로 설계되어있다.
  - 스프링 시큐리티는 내부에 여러 개의 필터가 Filter Chain이라는 구조로 Request를 처리하게 된다. 


- 💬 AuthenticationManager ?
  - Spring Security에서 핵심적인 동작을한다.
  - 실제 사용자 검증을 하는 Filter이다.
  - 인증(Authentication)이라는 타입의 객체로 작업을 진행한다.
  - AuthenticationManager가 가진 인증처리 메서드에서는 Parameter를 Authentication 타입을 받고 반환 타입 또한   
  👉 Authentication를 반환한다! **Parameter 와 Return Type이 같음!**
  - 실제 동작에서 전달되는 Parameter는 ...Token 과 같은 **토큰이라는 값으로 전달된다.**  
  👉 Spring Security의 필터의 주요 역할이 **인증 관련된 토근이라는 객체로 만들어서 전달한다는 의미이다.**
  - **AuthenticationProvider**를 사용하여 다양한 방식으로 인증 처리방법을 제공한다.
    - DB를 사용한 인증
    - 메모리상의 정보를 사용할 것인지 등등 ..


- 💬 AuthenticationProvider ?
  - AuthenticationManager의 하위에 있는 자식 개념으로 보면된다.
  - AuthenticationManager -> ProviderManager -> AuthenticationProvider ***[ 여러개가 존재함 ]***
  - 전달되는 토근의 타입을 처리할 수 있는 존재인지 확인하고, 이를 통해 authenticate()를 수행함.
  - 다양한 인증 처리를 할 수 있는 **객체들**을 가지고 있는 구조임.


- 💬 인가(Authorization)와 권한/ 접근제한
  - 위에서의 인증처리 단계까 끝나면 사용자의 권한이 적절한지에 대한 처리를 하는 부분이다.
  - 인증(Authentication)이 사용자가 스스로 자신을 증명하는 것이라면 인가(Authorization)은 그후의 권한에 따른 접근에 맞는 허가 개념이다.


- 💬 PasswordEncoder ?
  - 패스워드를 인코딩 하는 객체이다.
  - Spring Boot 2.0이상부터 인증위해 필수로 있어야한다.
  - PasswordEncoder는 인터페이스로 설계되어 있으며 설정에서 이를 구현하거나  
   구현된 Class를 이용해야한다.

\- Security Config Class - PasswordEncode  🔽
```java
//java - Security Config 
@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
public class SecurityConfig {

  /**
   * Spring Boot 2.0 이상의 버전에서 부터
   * Security 인증에는 반드시 PasswordEncoder를 필요로 하기에
   * 해당 객체를 Bean에 등록해줘야한다!
   * */
  @Bean
  PasswordEncoder passwordEncoder(){
    // bcrypt는 해시 함수를 이용하여 암호화하는
    // 단반향 암호화 Class이다.
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity.build();
  }

}
```

<br/>

\- Password Encode Test  🔽
```java
//java - JUnit Test

@SpringBootTest
public class PasswordEncodingTest {
    
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Test
  public void passwordEncodeTests(){
    // set Password
    String password = "1111";
    
    // encode password - used Bcrypt
    String enPw = passwordEncoder.encode(password);
    
    log.info("enPw ::: {}",enPw);
    
    //match password : ( basePw , encodePw )
    boolean pwMatchResult = passwordEncoder.matches(password, enPw);
    
    log.info("pwMatchResult ::: {} ", pwMatchResult);
  }
}
```

<br/>

\- 인가(Authorization) 설정 🔽
```java
//java - Spring Security Config

@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
public class SecurityConfig {

  /**
   * Spring Boot 2.0 이상의 버전에서 부터
   * Security 인증에는 반드시 PasswordEncoder를 필요로 하기에
   * 해당 객체를 Bean에 등록해줘야한다!
   * */
  @Bean
  PasswordEncoder passwordEncoder(){
    // bcrypt는 해시 함수를 이용하여 암호화하는
    // 단반향 암호화 Class이다.
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

    /**
     * httpSecurity.authorizeRequests()를 사용하여 인증이 필요한 자원들을 설정할 수 있다.
     * */
    httpSecurity.authorizeRequests()
            /**
             * antMatchers("targetUrl"); <<- 엔트 패턴으로도 지정이 가능하다
             * */
            // 누구나 로그인 없이도 /sample/all 에 접근 가능
            .antMatchers("/sample/all").permitAll()
            //User 권한을 갖으면 /sample/member 에 접근 가능
            .antMatchers("/sample/member").hasRole("USER");

            //.and()      //and()를 사용하면 체닝이르 옵션을 추가할 수있다.

            /***
             *  formLogin() 이란 ?
             *  - 인가, 인증에 문제가 생길시 로그인 페이지로 이동하끔 함
             *
             * ❌ formLogin()의 단점은 해당 매서드를 이용하는 경우에는 별도의 디자인을 적용 불가능한 Spring Security에서
             *    제공하는 UI를 사용해야함
             *
             * 👉 로그인 화면 , 성공 실패 등 URL을 지정하고 싶다면 ?
             *    loginPage()
             *    , loginProcessUrl()
             *    , defaultSuccessUrl()
             *    , failureUrl()
             *     등을 이용하면 필요한 설정을 지정할 수있다.
             *    - 대부분의 어플리게이션은 고유의 디자인을 적용하기 떄문에 loginPage()를 이용해 별도의 페이지를 만들어 사용!
             **/
             httpSecurity.formLogin();

    return httpSecurity.build();
  }

}


```

<br/>

- 💬 CSRF란 ?
  - Spring Security는 기본적으로 Cross Site Request Forgery - 크로스 사이트 요청 위조 공격을 막기위해  
  임의의 값을 만들어 GET요청을 **제외한 모든 요청에 포함시켜야 동작된다.** 
  - 서버에서 받아들이는 정보가 사전 조건을 검증하지 않는다는 점을 이용하는 공격 방식이다.
    - 사용 예시
      - 해커가 어떠한 사이트의 권한을 업데이트하는 URI가 존재한다는 것을 알아냈다.
      - 공격자는 관리자가 자주 방문 하는 사이트에 Image 혹인 form Tag에 위에서 말한 권한을 업데이트하는 URI를 심어놓는다.
      - 그렇게되면 자주 방문한 사이트에 접속하는동시 권한을 update하는 요청을 하게되어 공격자의 권한이 바뀌게 된다.
- 👍 이에 Spring Security에서는 CSRF Token 이라는 값을 발행해주며 한 세션당 하나의 토근이 생성되기에  
보안상 문제가 없고 token값을 함께 요청하지 않으면 되자 않기에 강제로 URI를 연결하거나 form Tag를 사용해도 문제가 없는것이다.
- 👉 단 해당 프로젝트에서 JWT를 사용할 예정이므로 Csrf는 disable 시킨 후 진행한다. 

\- Csrf 미사용 설정 🔽
```java
//java - Security Config

public class SecurityConfig {
    // ... code ...
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

    // ... code ...
    
    // CSRF를 사용하지 않도록 설정
    httpSecurity.csrf().disable();

    return httpSecurity.build();
  }
  // ... code ... 
}
```

<br/>

\- Logout 설정 🔽
```java
//java - Security Config

public class SecurityConfig {
    // ... code ...
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

    // ... code ...

    /**
     *   Logout 설정
     * -  로그아웃 페이지 생성
     * ❌ logout()의 단점은 fromLogin 과 같이 별도 디자인 적용 가능
     *
     *  👉 커스텀 페이지를 사용하려면  ?
     *  logoutUrl()
     *  , logoutSuccessUrl()
     *   등으로  커스텀 페이지 제작 가능
     *
     *   👉 로그아웃 시 쿠키 및 세션 삭제
     *   invalidateHttpSession(Boolean)
     *   , deleteCookies()
     *   를 사용하여 로그웃시 쿠키및 세션을 무효하 설정도 가능하다
     *   ex) httpSecurity.logout().deleteCookies().invalidateHttpSession(true);
     *
     * */
    httpSecurity.logout();

    return httpSecurity.build();
  }
  // ... code ... 
}
```

<br/>
<hr/>
<h3>2 ) Spring Security - DB사용 </h3>

- 회원의 권한 1:1이 정상적인 구조지만 테스트를 위해 한명의 회원이 한가지 이상의 권한을 갖을 수 있도록 구성하여 테스트
- Entity 구조
  - ClubMember : 회원의 로그인 정보를 갖는 Entity Class
  - ClubMemberRole : 프로젝트에서 사용될 회원의 권한들을 갖는 Entity Class
    - 단 해당 Class는 별로로 Entity Class로 만들기보다는 @ElementCollection을 사용하여 별도의 PK 생성 없이 구성함


- @ElementCollection란 ?
  - 💬 간단하게 설명해서 값 타입을 컬렉션에 담아 사용하는 것이다
  - RDB에서는 컬렉션과 같은 형태를 처리할경우 **별도의 Table을 생성하여 컬렉션을 관리하는데**  
  이와 같은 처리를 JPA에서는 @ElementCollection로 지정해주는것이다!