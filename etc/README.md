# ETC 

## @JsonNaming
```properties
# ✅ Jackson 라이브러리에서 제공하는 어노테이션이다.
#     객체 직렬화(Serialization) 및 역직렬화(Deserialization) 시 속성 이름의 변환 규칙을 지정하는 데 사용된다.
```
- JSON 데이터의 속성 이름 형식(예: snake_case, camelCase)을 클래스 필드와 다르게 지정해야 할 때 사용
### 네이밍 전략 종류

- **SNAKE_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)`
  - 변환 예시: `firstName` → `first_name`

- **UPPER_SNAKE_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)`
  - 변환 예시: `firstName` → `FIRST_NAME`

- **LOWER_CAMEL_CASE** *(기본값)*
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)`
  - 변환 예시: `firstName` → `firstName`

- **UPPER_CAMEL_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)`
  - 변환 예시: `firstName` → `FirstName`

- **LOWER_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)`
  - 변환 예시: `firstName` → `firstname`

- **KEBAB_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)`
  - 변환 예시: `firstName` → `first-name`



### 설정 방법
- 응답 값
  - `{"AppleID":"BlackGom","FlagBool":true,"NumberCd":100,"SakuraNum":"Hahahah100"}`
    - **Key 값**이 **대문자로 시작**하는 `UpperCamelCaseStrategy`형태
```java
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