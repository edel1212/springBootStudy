# springBootStudy


- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic) ]** :: 코드로 배우는 스프링 부트
  - JPA
  - query dsl
  - spring security
  - thymeleaf

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/exceptionStudy) ]** :: 예외 처리
  - Global Exception Handler
  - Filter 내 Exception 처리

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/p6spyStudy) ]** :: P6spy 적용

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/dbStudy) ]** :: DB
  - mybatis 적용
  - 다중 데이터베이스 적용

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/exGraphQL) ]** :: GraphQL 사용 방법 및 RESTAPI 차이점
  - 1 ) GraphQL 이란❔
  - 2 ) Rest API 와 GraphQL의 차이
  - 3 ) GraphQL의 장단점
  - 4 ) Spring For GraphQL 설정 및 사용 방법

<hr/>


- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/webSocketStudy) ]** :: WebSocket
  - 1 ) WebSocket
  - 2 ) SocketJS


<hr/>

### Docker를 이용한 배포 시 주의사항

#### 문제사항
- DB 연결이 되지 않는 문제가 발생하였다.
  - 기존 DB URL : `spring.datasource.url=jdbc:mariadb://localhost:3303/login`

- 원인 
  - 첫번째 
    - Mariadb의 경우 Docker를 사용하여 컨테이너로 올려 사용중이였다. 하지만 SpringBoot 어플레이케이션 또한 컨테이너로 올리게 되면  
    각각 컨테이너터 간의 연결이 필요할 경우 `dorcker network`를 생성하여 `bridge`방식으로 연결을 헤줘야한다. 
      - 😅 **하지만** --network 옵션을 통해 각각의 docker image를 연결해 주었지만 인식을 못하는 문제가 발생하였다.
        - Ex) `docker run -d -p 3303:3303 --name db --network back-net mariadb`  ## DB 컨테이너 생성  
        `docker run -d -p 8801:8801 --namd backServer --network back-net 이미지`  ## Server 컨테이너 생성
      - ✅ **이유**
        - 기존에 DB설정을 `localhost:3303`이 아닌 내가 생성한 network의 연결된 DB의 IP 혹은 Bridge의 IP를 연결해줘야한다.
        - 👉 **컨테이너와 컨테이너의 연결에는 반드시 브릿지가 필요**하기 떄문임
          - ip 확인
            - `docker inpect bridge` 혹은 `docker inspect 내가 만든 네티워크` 

✅ 변경된 DB URL  
```properties

server.port = 8081

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

### bridge IP
spring.datasource.url=jdbc:mariadb://172.17.0.1:3303/login

### 내가만든 Network IP - 둘다 접근 된느것 확인 완료
##spring.datasource.url=jdbc:mariadb://172.21.0.1:3303/login

### 운영
###spring.datasource.url=jdbc:mariadb://localhost:3303/login

```
