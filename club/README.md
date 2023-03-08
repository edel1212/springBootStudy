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
<h3>2 ) Spring Security - *DB사용* [ 설정 및 설명 ]</h3>

- 회원의 권한 1:1이 정상적인 구조지만 테스트를 위해 한명의 회원이 한가지 이상의 권한을 갖을 수 있도록 구성하여 테스트
- Entity 구조
  - ClubMember : 회원의 로그인 정보를 갖는 Entity Class
  - ClubMemberRole : 프로젝트에서 사용될 회원의 권한들을 갖는 Entity Class
    - 단 해당 Class는 별로로 Entity Class로 만들기보다는 @ElementCollection을 사용하여 별도의 PK 생성 없이 구성함


- @ElementCollection란 ?
  - 💬 간단하게 설명해서 값 타입을 컬렉션에 담아 사용하는 것이다
  - RDB에서는 컬렉션과 같은 형태를 처리할경우 **별도의 Table을 생성하여 컬렉션을 관리하는데**  
  이와 같은 처리를 JPA에서는 @ElementCollection로 지정해주는것이다!

<br/>

\- Insert ClubMember DummyData 🔽
```java
//java - JUnit Test : Insert Dummy Data

@SpringBootTest
public class ClubMemberTests {

  @Autowired
  private ClubMemberRepository clubMemberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  @Description("Insert Dummy Member Date")
  public void insertClubMemberTest(){

    IntStream.rangeClosed(1,100).forEach(i->{
      ClubMember clubMember = ClubMember.builder()
              .email("user"+i+"@naver.com")
              .name("User"+1)
              .fromSocial(false)
              .password(passwordEncoder.encode("1111"))
              .build();
      //권한 추가 - 기본적으로 USER 권한을 줌
      clubMember.addMemberRole(ClubMemberRole.USER);

      if(i > 80){
        clubMember.addMemberRole(ClubMemberRole.MANAGER);
      }
      if(i > 90){
        clubMember.addMemberRole(ClubMemberRole.ADMIN);
      }
      //Insert Member
      clubMemberRepository.save(clubMember);
    });

  }

}
```

<br/>

\- Find ClubMember Use Email, Social Flag 🔽
```java
//java - Repository

public interface ClubMemberRepository extends JpaRepository<ClubMember,String> {

  /**
   * ClubMember의 roleSet은 지연로딩으로 설정되어 있는데 해당 컬럼만을 로딩방법을
   * EAGER로딩으로 바꾸어 Proxy객체가 아닌 같이 SELECT 할수 있게끔 설정함
   * */
  @EntityGraph(attributePaths = "roleSet", type = EntityGraph.EntityGraphType.LOAD)
  @Query("SELECT m FROM ClubMember m WHERE m.fromSocial = :social AND " +
          "m.email = :email")
  Optional<ClubMember> findByEmail(String email, boolean social);

  /**** Result Query
   *  💬 JPQL Query에서 Join을 사용하지 않았지만
   *        @EntityGraph를 사용하여 LEFT OUTER JOIN이 적용된것을
   *        확인 할 수있다.
   * Hibernate: 
       select
         clubmember0_.email as email1_1_,
         clubmember0_.mod_date as mod_date2_1_,
         clubmember0_.reg_date as reg_date3_1_,
         clubmember0_.from_social as from_soc4_1_,
         clubmember0_.name as name5_1_,
         clubmember0_.password as password6_1_,
         roleset1_.club_member_email as club_mem1_2_0__,
         roleset1_.role_set as role_set2_2_0__ 
       from
        club_member clubmember0_ 
        left outer join
        club_member_role_set roleset1_ 
       on clubmember0_.email=roleset1_.club_member_email 
       where
        clubmember0_.from_social=? 
        and clubmember0_.email=?
   */
}


////////////////////////////////////////////////////////////////////


//java - JUnit Test
public class ClubMemberTests {
  @Description("Email을 사용하여 회원 찾기")
  @Test
  public void findByEmailToUserTest() {
    Optional<ClubMember> result = clubMemberRepository
            .findByEmail("user96@naver.com", false);

    result.ifPresent(log::info);
  }
}  
```

<br/>

- 💬 Spring Security에서는 기존 로그인과는 다르게 동작하는 것을 확인할 수있다.
  - 일반적 로그인 구현은 아이디와 패스워드가 일치 시 해당 정보를 세션이나 쿠키에 값을 저장하여 처리하는 형태지만  
  Spring Security에서는 조금 다르게 동작하며 몇가지 특이한점이 있다.
    - 1 . 회원이나 계정에 대하여 **User라는 용어를 사용한다.** ☠️ 따라서 User라는 단어를 사용할 떄는 주의가 필요하다.   
    - 2 . 회원아이디라는 용어 대신 username이라는 단어를 사용한다.  
      - 👉 간단하게 설명 : 일반적으로 사용하는 ID가  username으로 사용하는 것이다.   
      단 Security내의 **필터를 수정하여 해당 값에 주입하는 방식으로 변경하여 사용이 가능**하다.
    - username 과 passwrod를 동시에 사용하지 않는다.
      - Spring Security의 인증 절차는 UserDetailsService에서 username를 사용하여 회원의 존재를 먼저 가져온다
        - 이후 가져온 회원의 정보를 password와 비교후 틀리다면 "Bad Credential(잘못된 자격증명)" 값을 반환한다.
    - 3 . 위의 과정을 거쳐 화원의 인증이 완료돠면 요청된 행위의 권한을 확인하여 결과를 반환해 준다.
      - 인증이 완료되어도 권한이 없는 곳에 접근 시 "Access Denied"를 반환 하여 접근이 불가능함. 


\- UserDetailsService 구현 🔽
```java
//java - Service

@Log4j2
@Service
public class ClubUserDetailsService implements UserDetailsService {
    
    /**
     * @Service 어노테이션을 통해서 해당 Class를 스캔 하게되면
     * 구현한 UserDetailsService 또한 Bean에 등록되고 이에 따라서
     * 👉  자동으로 스프링 시큐리티에서 UserDetailsService를 해당 Class에 적성된
     *     @Override된 메서드를 실행하게된다.
     *     
     * ✅ 현재 상태로 로그인 시 로그인이 정상 기능을 하지 못한다.
     *    loadUserByUsername() -> null을 반환하기 떄문임
     *    하지만 log를 확인해보면 내가 로그인하려고 접근했던 ID를 확인할 수 있다.
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUSerByUserName ::: {}", username);
        return null;
    }
}
```


<br/>

- 💬 UserDetails Interface ?
  - 위에서 언급한  인증을 해주는 가장 핵심적인 로직은 UserDetailsService 이다.
  - UserDetailsService는 loadUserByUserName()이라는 단 하나의 메서드를 가지고있다.
  - loadUserByUserName()는 메서드명에서 확인 할수있듯이 회원의 ID(username)을 사용하여 회원의 정보를 가져온다.
    - 가져올 수 있는 정보
      - getAuthorities() - 사용자가 가지는 권한 정보
      - getPassword() - 인증을 마무리하 귀한 패스워드 정보
      - getUsername() - 인증에 필요한 아이디와 같은 정보


<br/>

- Spring Security를 사용하여 로그인 구현 방법은 크게 2가지 방법이 있다.
  - 1 . DTO 클래스에 UserDetails 인터페이스를 구현하는 방법
  - 2 . DTO와 같은 개념으로 별도의 클래스르 구성하고 이를 활용하는 방법
- 1번 방법이 조금 더 간단하기에 1번을 사용하려한다.
  - 👉 UserDetails를 구현해 놓은 Class가 여러가지가 있기 때문에 이를 활용함.
    - InetOrgPerson, LdapUserDetailsIsImpl, Person, User
- ✅ 그중에 User라는 구현 Class를 사용함  

\- ClubMemberDTO -> User class 상속 🔽
```java
//java - DTO Class

@Log4j2
@Getter
@Setter
@ToString
public class ClubMemberDTO extends User {
  /**
   * ClubMemberDTO 생성자 메서드에서 필수로
   * 부모 클래스 User의 생성 데이터를 요청하므로 [ super(); ]
   * 반드시 호출된다.
   * */  
  public ClubMemberDTO(
          String username
          , String password
          , Collection<? extends GrantedAuthority> authorities) {
    /**
     * User 생성자에서 인증에 필요한 정보인 
     * id, pw, 권한을 필요로한다.  
     * */  
    super(username, password, authorities);
  }
  
}
```

<br/>

\- ClubMemberDTO -> User class 상속  : DTO으로써 기능 추가🔽
```java
//java - DTO class

@Log4j2
@Getter
@Setter
@ToString
public class ClubMemberDTO extends User {
  
    /**
     * DTO에 필요한 정보를 추가해 줌으로써 DTO로써 의 기능도 수행하면서
     * 
     * 👉 User를 상속받아 UserDetails로써의 인가, 인증 작업 또한
     *    가능하게 되었다.
     * **/
    
  private String email;
  private String password;
  private boolean fromSocial;

  public ClubMemberDTO(
          String username
          , String password
          , boolean fromSocial
          , Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);

    this.email = username;
    this.fromSocial = fromSocial;
    
    ///////////////////////////////////////////////
    //☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️
    // 이거 틀린말임 .. 혼자서 판단하지 말자 ..
    // password를 따로 주입해주지 않는 이유는 인가, 인증의 기능은
    // 상단의 부모생성자(User)가 처리해주기에 해줄 필요가 없기 때문이다.
    //☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️
    ////////////////////////////////////////////////
    /**
     * ☠️ password 주입하지 않아 삽질함...
     * Error Msg : empty encode password error...
     *
     * 나의 생각은 DTO를 만들때 UserClass에서 알아서 확인해주므로
     * password 주입이 필요없다 생각했는데 아니였다 ..
     * Security Config에서 설정한 AuthenticationManager 부분의 설정중에
     * Service의 PW를 읽는 부분이 있는데 해당 부분을 먼저 거친다음 가기 떄문에
     * this.password = password 지정이 필요하다!!
     * */
    this.password = password;
  }
}
```

<hr/>

<h3>3 ) Spring Security - DB사용 [ 사용 코드 및 흐름 ]</h3>

\- Security Config 🔽

- 💬 userDetailsService를 구현한 ClubUserDetailsService를 주입하여 사용해줘야한다.
- 💬 AuthenticationManager 객체를 생성 및 적용 해줘야한다. [ ClubUserDetailsService 주입 해줄 대상 ]

```java
//java - Security Config

@Configuration 
public class SecurityConfig {
    
  @Bean
  PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  /**
   * ✅ 1. 변경하고싶은 로직을 작성한 Class인 UserDetailsService를 구현한
   *       ClubUserDetailsService를 주입하여 사용함
   * */
  @Autowired
  private ClubUserDetailsService clubUSerDetailService;

  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

    /***
     * @Description : Spring-boot 의 버전이 올라가면서 authenticationManger() 주입법이 바뀜.
     *               - 이전에는 해당 Class에 상속관계인 WebSecurityConfigurerAdapter 에서
     *                 구현된 메서드라 따로 수정없이 사용이 가능했지만 현재는 deprecated 되어서
     *                 👉 따로 ClubUSerDetailsService를 주입 받아 AuthenticationManager 객체를
     *                    생성해줘야한다.
     *                    
     *                ✅ 2. 꼭 지정해줘야한다 ! 안그러면 내가 작성한 UserDetailsService를
     *                      읽지 못한다!!
     * */
    AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(clubUSerDetailService)
            .passwordEncoder(this.passwordEncoder())
            .and()
            .build();
    httpSecurity.authenticationManager(authenticationManager);

    httpSecurity.authorizeRequests()
            .antMatchers("/sample/all").permitAll()
            .antMatchers("/sample/member").hasRole("USER")
            .and()      
            .formLogin();
    
    httpSecurity.csrf().disable();
    
    httpSecurity.logout();

    return httpSecurity.build();
  }

}
```

<br/>

\- UserDetails를 상속 구현한 DTO 🔽

- 💬 실질석인 인증 과정은 UserDetails를 구현한 Class인 User이다 따라서 해당 클래스를 상속받아   
    인증과정을 DTO에 합친것이다.
- 기존 Spring Security에는 이름, 소셜구분 , 그리고 Id가 username으로 되어있는데  
이 부분을 User 생성자에서 유동적으로 필요한 값에 맞춰서 넣어주고 DTO에 추가적인 정보를 넣을수있다. 
```java
//java - DTO

public class ClubAuthMemberDTO extends User {

  private String email;
  private String password;
  private String name;
  private boolean fromSocial;

  public ClubAuthMemberDTO(
          String username
          , String password
          , boolean fromSocial
          , Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);

    this.email = username;
    this.fromSocial = fromSocial;
    /**
     * ☠️ password 주입하지 않아 삽질함...
     * Error Msg : empty encode password error...
     *
     * 나의 생각은 DTO를 만들때 UserClass에서 알아서 확인해주므로
     * password 주입이 필요없다 생각했는데 아니였다 ..
     * Security Config에서 설정한 AuthenticationManager 부분의 설정중에
     * Service의 PW를 읽는 부분이 있는데 해당 부분을 먼저 거친다음 가기 떄문에
     * this.password = password 지정이 필요하다!!
     * */
    this.password = password;
  }
}
```

<br/>

\- UserDetailsService를 구현한 Service 🔽

- DB를 사용한 로그인을 가능케 한다.
- 로그인 과정 [ 간단 요약 ]
  - 1 .로그인 시 form의 정보를 토대로 UsernamePasswordAuthenticationFilter.java의 attemptAuthentication()로 접근
  - 2 . 받은 username과 password를 사용하여 UsernamePasswordAuthenticationToken 객체 생성
  - 3 . DB에서 해당 계정을 확인하고 계정이 존재하는지 확인
  - 4 . 존재한다면 해당 정보를 사용하요 UserDetails를 사용하여 계정의 정보와 2번에서 생성한 UsernamePasswordAuthenticationToken 비교
```java
//java - UserDetailsService

@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

  private final ClubMemberRepository clubMemberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("ClubUserDetailsService loadUSerByUserName ::: {}", username);

    Optional<ClubMember> result = clubMemberRepository.findByEmail(username,false);

    if(result.isEmpty()){
      log.info("?????");
      throw new UsernameNotFoundException("Check User Name");
    }//if

    ClubMember clubMember = result.get();

    log.info("clubMember Info ::: {}",clubMember );


    ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
            clubMember.getEmail(),
            clubMember.getPassword(),
            clubMember.isFromSocial(),
            clubMember.getRoleSet().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                    .collect(Collectors.toList())
    );

    clubAuthMember.setName(clubMember.getName());
    clubAuthMember.setFromSocial(clubMember.isFromSocial());

    return clubAuthMember;
  }
}


/////////////////////////////////////////////////////////////////////////////////////////

//java - Security Filter
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ...code ...
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
      if (this.postOnly && !request.getMethod().equals("POST")) {
        throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
      }
      String username = obtainUsername(request);
      username = (username != null) ? username.trim() : "";
      String password = obtainPassword(request);
      password = (password != null) ? password : "";
      UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
              password);
      // Allow subclasses to set the "details" property
      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    }
    // ...code ...
}


```

<br/>
<hr/>

<h3>4 ) Security 정보 확인 Client , Server</h3>

\- Client단 에서 확인하기 위해서는 build.gradle에 설정이 필요하다. 🔽
```groovy
//build.gradle

// ..code..

dependencies {

  // .. code ..
  
  /*현재  springsecurity6 버전에는 문제가 있어서 5버전으로 버전을 낮춘 후 개발 진행 - 화면에서 security 컨트롤 가능*/
  implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
    
  // .. code ..
}

// .. code..
```

<br/>

\- Client단에서의 사용 🔽
```html
<!-- html -->

<body>
<h1>For Member</h1>

<!-- 권한에 따라 보이고 안보이고 -->
<div sec:authorize="hasRole('USER')"> Has USER ROLE</div>
<div sec:authorize="hasRole('MANAGER')"> Has MAMAGER ROLE</div>
<div sec:authorize="hasRole('ADMIN')"> Has ADMIN ROLE</div>

<hr/>

<!-- 인증이 되었다면 보이게한다. -->
<div sec:authorize="isAuthenticated()">
  Only Authenticated user can see this Text
</div>

<hr/>

<!-- 가지고 있는 권한 목록 -->
principal  :
<div sec:authentication="principal"></div>

<hr/>

<!-- User Id-->
Authenticated username :
<div sec:authentication="name"></div>

<p></p>
<hr/>
<p></p>

Authenticated user roles :
<div sec:authentication="principal.authorities"></div>

</body>
</html>
```

<br/>

\- Server단에서의 사용 🔽
```java
//java - Controller

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {
    //... code ...
    
    /**
     * 👉 @AuthenticationPrincipal 어노테이션을 통해서 인증에 성공된 사용자의 정보를 받아 올 수 있다.
     *    - UserDetails를 구현한 객체로 정보를 받을 수 있다.
     *    - UserDetails 구현체의 정보는 Spring Security Context에 저장된 Authentication 객체가 가져간다고 볼 수 있다.
     * */
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
    //public void exMember(@AuthenticationPrincipal User user){  // User 자체로 받아서도 사용이 가능하지만 유연하게 사용이 불가능하다.   
      
      log.info("exMember....");
  
      log.info("로그인 정보 :::" + clubAuthMemberDTO);
  
    }
  
    //... code ...
}
```

<br/>
<hr/>

<h3>5 ) @PreAuthorize 란 ?</h3>
- Security Config에서 URL별 접근 권한 설정을 했지만 이 방법은 개수가 많아질수록 보기도 힘들어지고  
유지보수도 힘들다는 문제가있다.  
👉 ex) httpSecurity.authorizeRequests().antMatchers("/sample/member").hasRole("USER"); 
- 위와 같은 문제는 Controller에서 권한을 지정하고 싶은 요청에 @PreAuthorize를 설정 해주면 해결이 가능하다.
  - 💬 설정 방법
    - 1 . SecurityConfig에 @EnableGlobalMethodSecurity(조건) 추가
    - 2 . 권한을 설정하고 싶은 요청에 @PreAuthorize(권한) 추가  
    
\- @EnableGlobalMethodSecurity 설정 🔽
```java
//java - Security Config

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) 
public class SecurityConfig {
    
    /**
     * 💬 @EnableGlobalMethodSecurity ?
     *    - 어노테이션 기반의 접근을 제한을 설정할 수 있도록 하는 설정 어노테이션
     *    - Security 설정 클래스에 붙여 사용하는것이 일반적이다.
     *    - 설정 속 설정 의미
     *      - prePostEnabled = true 란 ?
     *          : Spring Security의 @PreAuthorize, @PreFilter , @PostFilter어노테이션 사용 하겠다는 의미이다.
     *          
     *      - securedEnabled = true 란 ?
     *          : @Secured 를 이용하겠다는 의미이다.
     *   
     *   //////////////////////////////////////////////////////////////
     *   
     *   ✅ @Secured, @PreAuthorize 차이는 ????      
     *   
     *   @Secured는 표현식 사용할 수 없고,      👎
     *   
     *   @PreAuthroize는 표현식 사용 가능하다.  👍
     *
     *   ex) 
     *       @Secured({"ROLE_USER","ROLE_ADMIN"}) => OR 조건, AND 조건 불가능
     *       @PreAuthorize("hasRole('ROLE_USER') and hasRole('ROLE_ADMIN')") => and 조건, or 조건 모두 가능       
     * */
    
}
```

\- @PreAuthorize 설정 🔽
```java
//java - Controller

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {
    
    // 👉 누구나 접근 가능
    @PreAuthorize("permitAll()") 
    @GetMapping("/all")
    public void exAll(){
      log.info("exAll.....");
    }

    // 👉 ADMIN만 접근 가능
    @PreAuthorize("hasRole('ADMIN')") //Admin 권한
    @GetMapping("/admin")
    public void exAdmin(){
      log.info("exAdmin.....");
    }

    /***
     * @Description : 특정 사용자만 사용해서 하고싶을떄
     *                1) Parameter 로 로그인 Info 를 받는다
     *                   조건 : 1 . @AuthenticationPrincipal 어노테이션을 사용하여 Parameter를 받야한다.
     *                         2 . 대상의 타입은 UserDetails를 구현한 객체여야한다.
     *                2) @PreAuthorize()  표현식을 사용하여 조건을 설정해야한다.
     *                   - '#'과 같은 특별한 기호와 authentication 같은 내장변수를 이용할 수있다.
     *                   
     *                👉 조건 해석
     *                   - UserDetails의 정보가 null이 아니고  username(id)가 "user95@naver.com"인 사용자
     * */
    @PreAuthorize("#clubAuthMemberDTO != null && #clubAuthMemberDTO.username eq \"user95@naver.com\"")
    @GetMapping("/exOnly")
    public String onlyTargetUser(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
      log.info("=======================");
      log.info("onlyTargetMember!!");
      log.info(clubAuthMemberDTO);
      log.info("=======================");
  
      return "/sample/admin";
    }
    
}
```

<br/>
<hr/>

<h3>6 ) 로그인 실패 시 설정 [ FailureHandler ] </h3>

- 로그인 실패시 Handler 설정이 필요하다.
- 해당 설정 방법
  - 1 . 설정된 Class를 Bean Container에서 Scan할 수 있도록 @Component 지정이 필요하다.
  - 2 . SimpleUrlAuthenticationFailureHandler를 상속이 필요하다.
  - 3 . onAuthenticationFailure() 를  @Override구현이 필요하다.
  
\- 로그인 실패 Handler Class 설정 🔽
```java
//java - extends SimpleUrlAuthenticationFailureHandler Class

/**
 * @Description : Security Exception 발생시 처리하는
 *                Handler class
 * */
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
    String errorMsg;
    if (exception instanceof BadCredentialsException) {
      errorMsg = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
    } else if (exception instanceof InternalAuthenticationServiceException) {
      errorMsg = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
    } else if (exception instanceof UsernameNotFoundException) {
      errorMsg = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
    } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
      errorMsg = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
    } else {
      errorMsg = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
    }

    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("status"   , "401");
    errorMap.put("errorMsg" , errorMsg);
    
    /**
     * 실패 시 JSON형식의 데이터를 만들어 반환 시키는 로직
     * */
    ObjectMapper objectMapper = new ObjectMapper();
    // errorMsg가 한글이기에 설정이 필요하다.
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(objectMapper.writeValueAsString(errorMap));

  }
}
```

<br/>

\- 로그인 실패 Handler Security Config 설정 🔽
```java
// java - Security Config

@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    
    //...code...
    
    //Login Fail Handler
    @Autowired
    private AuthenticationFailureHandler customAuthFailureHandler;
  
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
      //...code...

      httpSecurity.formLogin()
              .loginPage("/sample/login")                 // Login Page URL  [GET]
              .loginProcessingUrl("/sample/loginProcess") // 로그인 Request URL [POST]
              .failureHandler(customAuthFailureHandler);   // 실패 시 처리 Handler 지정
               
      /**
       * 로그인 인증에 실패시 URL을 지정해서 이동이 가능하나 현재 
       * 비동이 식으로 진행하기 때문에 Error Msg 및 상태 코드르 전달 하여 
       * 적용할 예정이기에 사용하지 않음
       * 
       * 👉 위의 방법 말고도 FailureHander에서도 지정이 가능하다.
       * 
       * .failureUrl("/")                            //실피 시 Direct 이동
       * */

      //...code...

      return httpSecurity.build();
      
    }
    
}
```


<br/>

\- FailureHandler 설정 🔽
```java
//java - onAuthenticationFailure() @Override 구현

/**
 * @Description : Security Exception 발생시 처리하는
 *                Handler class
 * */
@Component // Scan 대상 지정
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {

    String errorMsg;
    if (exception instanceof BadCredentialsException) {
      errorMsg = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
    } else if (exception instanceof InternalAuthenticationServiceException) {
      errorMsg = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
    } else if (exception instanceof UsernameNotFoundException) {
      errorMsg = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
    } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
      errorMsg = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
    } else {
      errorMsg = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
    }

    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("status"   , "401");
    errorMap.put("errorMsg" , errorMsg);

    ObjectMapper objectMapper = new ObjectMapper();
    //해당 설정을 해주지 않으면 요청받은 Message가 꺠져 나오는 문제가 있음
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(objectMapper.writeValueAsString(errorMap));

  }

}
```

<br/>

\- Client Login요청 및 처리 🔽
```html
<!-- html -->
<body>
    <input type="text" name="username"/>
    <input type="passowrd" name="password"/>
    <button id="loginBtn">로그인</button>
</body>

<script>
  
  document.querySelector("#loginBtn").addEventListener("click",(e) => {
            /**
             * URLSearchParams 객체는 키/밸류로 쌍으로 구성된 데이터들을 관리하기 위해 제공되는 객체입니다.
             * JSON 데이터를 표현하는 것과 같은 방법으로 서술한 키/밸류 데이터를 URLSearchParams 객체 생성자 인수로 넘겨서 객체를 생성합니다.
             * 그리고 이 URLSearchParams 객체는 비동기 통신을 할 때 객체 그 자체를 폼 데이터로 넘겨서 전송할 수 있습니다.
             *
             *  👉 인코딩까지 자동으로 해주기 때문에 별도로 전송 데이터를 인코딩하는 수고로움을 하지 않아도 됩니다.
            */
            const param = new URLSearchParams({
                username : document.querySelector("input[name='username']").value
                , password : document.querySelector("input[name='password']").value
            })

            console.log(param);
            fetch("/sample/loginProcess"
                ,{ method: "POST"
                   , cache : "no-cache"
                   ,"Content-Type": "application/x-www-form-urlencoded"
                   //,"Content-Type": "application/json; charset=UTF-8" ❌ 해당 방법은 인식을 하지못함
                   , body : param
                }
            ).then((data) => data.json())
            .then((result) => {
                console.log(result);
            })
            .catch((error) => {
                console.log(error)
            })

        });
  
</script>

```

<br/>
<hr/>

<h3>7 ) 로그인 성공 시 설정 [ SuccessHandler ] </h3>


\- 로그인 성공 Handler Security Config 설정 🔽
```java
//java - Security Config

@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  //...code...

  //Login Fail Handler
  @Autowired
  private AuthenticationFailureHandler customAuthFailureHandler;

  //Login Success Handler
  @Autowired
  private AuthenticationSuccessHandler customAuthSuccessHandler;
  
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
    //...code...

    httpSecurity.formLogin()
            .loginPage("/sample/login")                  // Login Page URL  [GET]
            .loginProcessingUrl("/sample/loginProcess")  // 로그인 Request URL [POST]
            .failureHandler(customAuthFailureHandler)    // 실패 시 처리 Handler 지정
            .successHandler(customAuthSuccessHandler);   // 로그인 성공 시 처리 Handler 지정

    //...code...

    return httpSecurity.build();

  }

}
```

<br/>

\- SuccessHandler 설정 🔽
```java
//java - onAuthenticationSuccess() - @Override 구현 Class

/**
 * @Description : Security Success 시 처리하는
 *                Handler class
 *
 *                👉 Failure Handler 와 다르게 SuccessHandler는 Interface이다!
 * */
@Component
@Log4j2
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
          , Authentication authentication) throws IOException, ServletException {

    log.info("Success!!");

    //TODO Business logic [ 비밀번호 오류 횟수 초기화등 여러가지 로직이 처리 가능하다. ]

    log.info("login user authentication :: " + authentication.getAuthorities());

    Map<String, String> result = new HashMap<>();
    result.put("status","200");
    result.put("Msg","Success");

    ObjectMapper objectMapper = new ObjectMapper();
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(objectMapper.writeValueAsString(result));

    /**
     * 해당 방법으로 리다이렉트 이동이 불가능하다.
     * 💬 Login 로직을 비동기 방식으로 진행했기에
     * 404 에러가 떨어짐 URL 이동은 스크립트로 처리가 필요하다
     * */
    //response.sendRedirect("/sample/member");

  }
}
```

<br/>
<hr/>

<h3>8 ) OAuth Login [ 소셜 로그인 ] </h3>

- 서비스를 제공하는 업체들이 각자 다른 방법으로 로그인을 하지 않도록 공통 인증 방식을 제공하는  
OAuth(Open Authorization)라고 한다.
- 해당 로그인 서비스를 이용하기 위해서는 선행 되어야하는 것이 있는데 그것은 바로 해당 서비스를 지원해주는 곳에 API에 사용 요청 후  
  ClientID와 Password를 받아야한다.

### Google OAuth 신청방법 ###
1. [GoogleCloud](https://console.cloud.google.com/apis) 로 이동하여 프로젝트를 생성해 준다.
2. 페이지 상단 로고 옆 버튼을 누르면 프로잭트 생성이 있다.
3. API사용 설정 클릭
4. 소셜 -> Contacts API 사용하기
5. OAuth 동의 화면 이동
6. User Type "외부"로 설정
7. 필요 정보를 작성 
8. 동의화면 작서잉 끝났다면 "사용자 인증정보"로 이동한다.
9. 상단 사용자 인증 정보 만들기
10. OAuth 클라이언트 ID
11. 애플리케이션 유형 "웹"
12. 가장 중요한 승인된 리디렉션 URI를 추가해준다  
 👉 구글에서 인증된 정보를 다시 현재 프로젝트로 정보를 반환받을 URI이다.
13. 생성된 ID와 Pw를  프로젝트에 적용한다.

### Google OAuth 적용방법 ###
1. build.gralde에 OAuth dependence 추가
```groovy
//build.gradle

dependencies {
  
  implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'\
  
  //..code...
}
```
2. properties 파일에 OAuth 관련 설정이 필요 단 application.properties 보다는  새로운 properties파일을  
만들어서 사용하는 것이 더 직관적이며 수정이 간편하므로 OAuth 설정을 위한 properties을 생성하여 작성한다.  
💬 생성 하려는 properties 네이밍시 application-xxx.properties 로 작성해야한다.  
**👉 중요  :  중간에 "-"가 아닌 다른 기호를 사용하면 안된다! 인식을 못함**  
   ☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️  
   ☠️ 삽질 내용 :  
   ☠️ 해당 properties 설정 시 삽질을 했었는데 그것은 바로 Scope Type 부분에서 문제 발생   
   ☠️ - 문제 Code 👎   
   ☠️ spring.security.oauth2.client.registration.google.client-scope=email ❌ 중간에 client가 들어가 있어서 문제 발생..   
   ☠️ - 수정 Code 👍    
   ☠️ spring.security.oauth2.client.registration.google.scope=email          ✅ 정상 작동안됨.  
   ☠️  
   ☠️ 💬 해당 문제를 찾는데 오래 걸린 이유는 빌드 및 이용 시에는 문제가 없었지만 커스텀을 위한 Class를 작성하여 작업도 중  
   ☠️ DefaultOAuth2UserService 를 상속 받아 @Override 구현한 loadUser() 메서드에 접근 조차 하지 않는 문제가 있었다...  
   ☠️  
   ☠️ 👉 생각없이 하지말고 흐름을 이해하면서 적용할 필요를 느꼈다 ..  
   ☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️☠️
```properties
#application-oauth.properties

##Google Client ID
spring.security.oauth2.client.registration.google.client-id=아이디 입력

##Google Client PW
spring.security.oauth2.client.registration.google.client-secret=패스워드 입력

##scope Type
spring.security.oauth2.client.registration.google.scope=email
```

3. 추가한 application-oauth.properties를 읽을 수 있도록 application.properties에 주입
```properties
#application.properties

#...code...

############################
##include oauth properties
############################
spring.profiles.include=oauth
```

4. SecurityConfig에서 oAuth 로그인을 사용 할 수 있도록 지정  
👉 커스텀 로그인에서도 가능하지만 현재 스프링에서 지원해주는 것을 사용하여 테스트함
```java
//java - SecurityConfig

@Configuration //BeanContainer에서 해당 Class를 스캔하도록 지정
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    //...code..
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
      //...code..

      httpSecurity.oauth2Login();
      
    }
    
}
```

### 현재 OAuth Login 문제점 및 해결 방법 ###
- 💬 문제점  
  - 기존 로그은의 경우 ClubAuthMemberDTO 객체를 사용하여 관리하였는데 현재 OAuth의 경우는 그렇지 않아  
  접근하려는 매핑 Method에서 사용하는 권한을 가져오는 (@AuthenticationPrincipal ClubAuthMemberDTO dto)의 값이  
  null인것을 확인 할 수 있다.
