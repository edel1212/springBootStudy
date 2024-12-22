# Exception 처리

## GlobalException Handler
- `@RestControllerAdvice`를 사용하면 Runtime 중 발생하는 Exception 처리가 가능하다.
- 발생 하는 예외를 지정 후 처리 가능
  - `@ExceptionHandler(발생 예외 클래스.class)`
- 일반적인 Runtime Exception 및 **CustomException울 잡아 처리**가 가능하다

### 주의 사항
- Filter 에서 발생하는 예외의 경우 처리가 불가능
  - 이유
    - @RestControllerAdvice는 Spring MVC의 요청-응답 흐름 내에서 작동하는 예외 처리 메커니즘입니다
      - **필터**는 이 흐름의 바깥에서 동작하기 때문에, 필터에서 발생한 예외를 @RestControllerAdvice에서 처리하지 못함
      - Spring MVC 요청-응답 흐름
        - `필터 -> DispatcherServlet -> 컨트롤러 -> Service/Repository -> 뷰 반환 -> DispatcherServlet -> 필터`
  - 처리 방법
    - Filter 로직 내에서 자체 예외 처리가 필요

### CustomException
```java
@NoArgsConstructor
public class ExistMemberException extends RuntimeException {
    public ExistMemberException(String message) {
        super(message);
    }
}
```

### Servoce
```java
class MemberServiceImpl{
    public void findMemberException(){
        Member member = memberRepository.findById(modMemberReq.getAccountId())
                .orElseThrow(NotFoundMemberException::new);    
    }
}
```

### DTO
```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class CommonResponse {
    private Integer code = 200;
    private String message = "success";

    public static CommonResponse success() {
        return new CommonResponse();
    }

    public static CommonResponse fail(ExceptionCode exceptionCode) {
        return new CommonResponse(exceptionCode.getCode(), exceptionCode.getMessage());
    }

    public static CommonResponse fail(int errorCode, String errorMessage) {
        return new CommonResponse(errorCode, errorMessage);
    }
}
```

### Controller
```java
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * <h3>잘못된 형식의 파라미터</h3>
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("잘못된 형식의 Type 입니다 : '%s' ->  '%s'의  알맞은 Type 은 '%s' 입니다.",
                ex.getValue(), ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        CommonResponse commonResponse = CommonResponse.fail(999, errorMessage);
        log.warn("잘못된 형식의 Type 전달: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }

    /**
     * <h3>json 파싱 에러</h3>
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "JSON 파싱 에러 파라미터를 확인해 주세요: " + ex.getMessage();
        log.error(errorMessage);
        CommonResponse commonResponse = CommonResponse.fail(998, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }

    /**
     * <h3>이미 존재하는 회원</h3>
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler(ExistMemberException.class)
    public ResponseEntity<CommonResponse> existMemberException(ExistMemberException e) {
        CommonResponse commonResponse = CommonResponse.fail(ExceptionCode.EXIST_MEMBER);
        log.warn(ExceptionCode.EXIST_MEMBER.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }
}
```

## 404 페이지 처리
-  `ErrorController` 구현을 통해 적용이 가능하다.
- MethodName은 수정이 가능하다 `RequestMapping`의 경우 "/error"의 값은 변경 X
  - RepuqseMapping 값 변경이 필요할 경우 application.properties 수정 필요
    - `server.error.path=/custom-error`

### 주의 사항
- 해당 예외 값 감지는 404 뿐만 아니라 **처리 되지 않은 500 에러 까지 잡음**
- `@RestControllerAdvice`를 사용해서 처리가 가능 구분하여 사용하는 이유
  - **Spring Rest Docs를 사용할 경우** 해당 resource에 **접근이 불가능**하여 해당 설정을 추가적을 진행하는 것보다는 예외를 구분하는 것이 더욱 효육적이라 판단.
    - 정적 리소스 경로(/docs/**)도 영향을 받움
  - @RestControllerAdvice 사용 404처리 방법
    - #### application.properties
        ```properties
        spring.mvc.throw-exception-if-no-handler-found=true
        spring.web.resources.add-mappings=false
        ```
    - #### Controller
      ```java
      @RestControllerAdvice
      @Log4j2
      public class GlobalExceptionHandler {
        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<CommonResponse> handle404(NoHandlerFoundException ex) {
            log.warn("404 Error: {}", ex.getMessage());
            CommonResponse commonResponse = CommonResponse.fail(ExceptionCode.NOT_FOUND_PAGE);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonResponse);
        }
      }
      ```

### Controller
```java
@Log4j2
@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<CommonResponse> handle404() {
        CommonResponse commonResponse = CommonResponse.fail(ExceptionCode.NOT_FOUND_PAGE);
        log.warn(ExceptionCode.NOT_FOUND_PAGE.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonResponse);
    }
}
```