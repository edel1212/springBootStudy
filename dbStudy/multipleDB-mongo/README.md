# 다중 DB 설정

```properties
# ℹ️ Maraidb(JPA), MongoDB를 사용해서 진행
#  - Port : 3306, 27017
```

## JPA 와 MongoRepository

```properties
# ✨ MongoDB의 경우 특별하게 Multiple DB 설정이 필요 없이 디팬던시와 Connection 설정만 진행 해주면 된다.
```

### build.gradle

```groovy
dependencies {
    // Mariadb
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    
    // MongoDB
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
}
```

### application.yml

```properties
spring:
  # Mariadb
  datasource:
    url: jdbc:mariadb://localhost:3306/test
    username: root
    password: 123
    driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update

  # MongoDB
  data:
    mongodb:
      uri: mongodb://admin:123@localhost:27017/book?authSource=admin
      auto-index-creation: true
```

### Entity, Repository 구성

#### JPA
- 모두 같기에 스킵

#### MongoDB
- MongoDB-Study - [참고](https://github.com/edel1212/mongoDB-Study]) 