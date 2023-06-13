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