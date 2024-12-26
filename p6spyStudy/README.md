# P6spy 적용

```properties
# ✅ SpringBoot3부터 적용방법이 변경
#    - 이유 : 스프링 부트의 자동 설정(auto-configuration) 방식이 변화했기 때문이다
```

## 기본 JAP SQL
- 간단하게 JPA 설정 시 한번에 설정 가능

### applicaion.yml
```yaml
  ## JPA setting
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        # ✔ 해당 옵션을 사용해야 format이 설정되어 보기 좋게 나옴
        format_sql: true
    show-sql: true
```

### Result Console
```shell
Hibernate: 
    insert 
    into
        member
        (name, id) 
    values
        (?, ?)
```
