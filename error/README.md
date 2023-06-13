# Error 모음

### API 통신간 DTO Null 이슈
- 문제
  - SpringBoot + Lombok 사용시 데이터를 받는 입장에서 분명 JSON 과 DTO의 Key 값이 같은데도 인식하지 못했던 문제
- 원인
  - DTO의 Key값을 맞춰줘도 Lombok에서 강제로 CamelCase로 변경함
- 해결 방법
  - DTO의 상단에 알맞은 `@JsonNaming(PropertyNamingStrategies.알맞은케이스.class)`를 사용
  
<br/>

❌ 예시 코드 - 문제가 있던 코드 
```java

// 👉 JSON 코드
// {"AppleID":"BlackGom","FlagBool":true,"NumberCd":100,"SakuraNum":"Hahahah100"}


// DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpperDTO {
    private String AppleID;

    private String SakuraNum;

    private Boolean FlagBool;

    private Long NumberCd;

}


///////////////////////////////////////////////////////////////


// Service - Webclient를 통한 서버간 통신
@Service
@Log4j2
public class UpperCamelServiceImpl implements UpperCamelService {
    @Override
    public UpperDTO getItem(Long code) {

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

        UpperDTO result = webClient.get().uri("/upper/" + code).retrieve().bodyToMono(UpperDTO.class).block();

        // 👎 null 값이 들어가있음..
        log.info("result ::: {}", result);
        
        return result;
    }
}

```


✅ 예시 코드 - 해결
- `@JsonNaming()`로 케이스를 변환하여 해결함
```java

// 👉 JSON 코드
// {"AppleID":"BlackGom","FlagBool":true,"NumberCd":100,"SakuraNum":"Hahahah100"}


// DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class UpperDTO {
    private String AppleID;

    private String SakuraNum;

    private Boolean FlagBool;

    private Long NumberCd;

}

```

<hr/>

### class java.util.LinkedHashMap cannot be cast to class 에러 이슈
- 문제
  - JSON형식이 `"Method":"메서드입니다", Result: [{}.....]`의 데이터를 DTO로 전환 시 문제 발생
  - `@JsonNaming()`를 사용해하면 class내부의 배열 부분을`List<대상클래스>`로 문제가 해결되었으나 <Generic>을 사용시 문제 발생
- 원인
  - SpringBoot 2.xx, 3.xx 버전의 문제 4.xx버전에는 고쳐졌다함.
- 해결방법
  - Generic을 사용하지 않고 해결
  - `objectMapper.convertValue(변결할 Data, 대상클래스.class);`를 사용하여 Object 를 DTO로 변환
    - 참고 :: https://moonong.tistory.com/61

<br/>

❌ 예시 코드 - 문제가 있던 코드
```java

// 👉 JSON 코드
// {"Method":"Mmmmmm","Result":[{"AppleID":"BlackGom","FlagBool":false,"NumberCd":1,"SakuraNum":"Hahahah1"}, ...]

// ParentClass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class BaseWrapDTO<T> {

    private String Method;

    private List<T> Result;

}

// ChildClass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class UpperDTO {
    private String AppleID;

    private String SakuraNum;

    private Boolean FlagBool;

    private Long NumberCd;

}


///////////////////////////////////////////////////////////////


// Service - Webclient를 통한 서버간 통신
@Service
@Log4j2
public class UpperCamelServiceImpl implements UpperCamelService {
    @Override
    public List<UpperDTO> getList() {

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

        BaseWrapDTO dto = webClient.get().uri("/upper").retrieve().bodyToMono(BaseWrapDTO.class).block();

        log.info("result ::: {}",dto.getResult().get(0).getFlagBool());
        log.info("result ::: {}",dto.getResult().get(0).getAppleID());

        return null;
    }
}
```  

✅ 예시 코드 - 해결
- `@JsonNaming()`를 API응답 데이터에 꼭 맞춰서 사용해주자 - 부모 자식 둘다
```java

// 👉 JSON 코드
// {"Method":"Mmmmmm","Result":[{"AppleID":"BlackGom","FlagBool":false,"NumberCd":1,"SakuraNum":"Hahahah1"}, ...]


// ParentClass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class BaseWrapDTO {

    private String Method;

    private List<UpperDTO> Result;

}

// ChildClass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class UpperDTO {
    private String AppleID;

    private String SakuraNum;

    private Boolean FlagBool;

    private Long NumberCd;

}

```