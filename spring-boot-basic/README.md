# 코드로 배우는 스프링 부트

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/ex01) ]** :: Spring Boot, Spring Framework 차이 및 설정 JUnit Test
    - 1 ) Gradle , Maven 차이
    - 2 ) Spring , Spring Boot 차이
    - 3 )  Port 변경 
    - 4 ) JUnit Test

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/ex02) ]** :: JAP, ORM, JPQL, Paging 및 DB설정 Docker를 사용 하여 DataBase 사용법
    - 1 ) ORM [ 객체 관계 매핑 (Object–relational mapping) ]❔
    - 2 ) JPA(Java Persistence API)❔
    - 3 ) JPA, DB 추가 및 DB접속 정보 설정
    - 4 ) Entity Class란❔
    - 5 ) JpaRepository란❔
    - 6 ) JpaRepository를 사용한 CRUD
    - 7 ) JpaRepository를 사용한 Paging
    - 8 ) QueryMethod
    - 9 ) @Query 어노테이션 [ JPQL ]
<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/ex03) ]** :: Thymeleaf 사용법, RedirectAttributes를 사용하여 Redirect 시  데이터 전달
    - 1 ) Thymeleaf 란❔
    - 2 ) Thymeleaf를 사용해 데이터 출력 [ 반복문 및 조건문]
    - 3 ) Thymeleaf를 사용해 데이터 출력 [ inline ]
    - 4 ) Thymeleaf의 링크 처리 및 레이아웃

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/ex04) ]** :: REST API, Swagger, RestTemplate, WebClient 사용 방법
    - 1 ) REST API 란❔
    - 2 ) @RestController 란❔
    - 3 ) @RequestMapping의 Consumes 와 Produces 란❔
    - 4 ) Content-Type의 application/json 과 application/x-www-from-urlencoded 차이점❔
    - 5 ) Swagger Setting
    - 6 ) RestTemplate 이란 ?
    - 7 ) WebClient 란❔

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/board) ]** :: N:1(다 : 1) 연관관계(Join 사용), @Query, querydsl(JPQLQuery 객체)
    - 1 ) @ManyToOne 연관관계 ❔
    - 2 ) 지연로딩(lazy Loading)의 장단점❔
    - 3 ) JPQL 과 left (outer) join
    - 4 ) Object[]를 DTO로 변경하기 - [ JPQL Pageing 반환 ]
    - 5 ) 연관관계가 있을 경우의 삭제
    - 6 ) QuerydslRepositorySupport의 JQPLQuery 사용 Join 및 Object[] 다루기
        - QuerydslRepositorySuppoert은 2가지 방법이 존재( JPAQueryFactory, JPQLQuery ) 차이가 있음

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/guestbook) ]** :: MappedSuperClass , Querydsl, 페이징 처리
    - 1 ) MappedSuperClass 란❔
    - 2 ) Querydsl ❔
    - 3 ) 서비스 계층과 DTO ❔
    - 4 ) 페이징 처리 DTO 사용
    - 5 ) @ModelAttribute 란❔

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/mreview) ]** :: M : N(다 대 다) [@EntityGraph] , 파일업로드 불러오기
    - 1 ) M : N(다 대 다) 관계의 특징
    - 2 ) M : N(다 대 다) 연관관계를 만들시 문제점
    - 3 ) M : N(다 대 다) 연관관계 해결방안 [ Row를 사용 수직으로 확장하면 된다! ]
    - 4 ) 매핑 테이블의 특징 ?
    - 5 ) JPA에서 M:N(다대다) 처리 방법 - 사전 준비
    - 6 ) 상단에 명시된 테스트 목록 테스트
    - 7 ) 파일 업로드 처리
<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic/club) ]** :: Spring Security, 소셜 로그인, JWT
    - 1 ) Spring Security
    - 2 ) Spring Security - *DB사용* [ 설정 및 설명 ]
    - 3 ) Spring Security - DB사용 [ 사용 코드 및 흐름 ]
    - 4 ) Security 정보 확인 Client , Server
    - 5 ) @PreAuthorize 란 ?
    - 6 ) 로그인 실패 시 설정 [ FailureHandler ]
    - 7 ) 로그인 성공 시 설정 [ SuccessHandler ]
    - 8 ) OAuth Login [ 소셜 로그인 ]
    - 9 ) JWT (JSON Web Token)
