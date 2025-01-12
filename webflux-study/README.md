# WebFlux 프로젝트

```properties
# ✅ 반응형 프로그래밍(Reactive Programming) 모델을 기반으로 하여, 높은 동시성 및 성능을 요구하는 시스템에서 효과적인 웹 애플리케이션을 개발할 수 있도록 돕습니다. 
``` 

## 주요 특징

- 비동기 및 논블로킹 처리:
  - WebFlux는 **논블로킹 I/O**와 **비동기 프로그래밍**을 지원하여, 서버의 자원을 더 효율적으로 사용 가능
    - 비동기 요청일 경우
      - 클라이언트의 요청이 들어오면, 서버는 다른 작업을 진행하면서 I/O 작업을 기다리지 않고 다른 요청을 처리 가능
        - 이는 높은 동시성 요청을 처리해야 하는 시스템에서 성능을 극대화할 수 있게 해줌
- 반응형 프로그래밍(reactive programming):
  - 반응형 프로그래밍은 데이터 흐름과 변화에 반응하는 방식으로, **데이터가 흐를 때마다 결과를 처리**
  - WebFlux는 Reactor라는 라이브러리를 사용하여 `Mono`와 `Flux`라는 **두 가지 주요 타입을 제공**
    - Mono
      - **단일 값** 또는 **빈 값**을 **비동기적으로 처리**하는 타입
    - Flux
      - **0개 이상의 값**을 **비동기적으로 처리**하는 타입
  - 이러한 타입을 사용하여 비동기적으로 데이터를 처리하고, 스트림을 통해 응답을 반환할 수 있습니다.
- 스케일링 및 성능:
  - WebFlux는 논블로킹 방식으로 동작하여, 여러 요청을 동시에 처리할 수 있어 시스템의 확장성이 뛰어남
    - 이로 인해 WebFlux는 특히 마이크로서비스 아키텍처나 높은 트래픽을 처리해야 하는 시스템에서 유리

## Spring MVC와의 차이점
- Spring MVC는 동기식 모델을 기반으로 하여 요청-응답을 처리
  - 이 방식은 요청이 들어오면 이를 처리할 때까지 다른 작업을 할 수 없음
- 반면 WebFlux는 **비동기 및 논블로킹 처리를 지원**하여, 요청을 비동기적으로 처리하고 **다른 작업을 동시에 처리** 
  - 이를 통해 대규모 시스템에서 성능과 확장성을 개선할 수 있음

## 설정 방법

### Dependencies

```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
}
```

### Config Class
- 해당 설정 Class 사용은 **필수가 아니다**.
  - 연계의 대상의 도메인 정보가 같을 경우에 활용하면 좋다.
- #### HttpClient
  - 해당 구성을 통해 `Netty`의 설정을 진행 가능하다.
  - responseTimeout : 전체 응답 대기 시간
  - option : 사용하려는 옵션 추가
  - doOnConnected : 상세 응답이 오지 않을 때 타임아웃 지정
    - `ReadTimeoutHandler, WriteTimeoutHandler` 예외를 통해 시간 지정
- ####  WebClient
  - baseUrl : 도메인 정보
  - clientConnector : 사용하는 clientConnector 지정
  - exchangeStrategies : HTTP 요청과 응답을 처리하는 방식에 대한 설정을 나타내는 옵션
    - 요청 및 응답 데이터를 처리할 때 사용하는 직렬화/역직렬화 전략을 설정 및 크기 설정 가능

````java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientBuild(){
        /**
         * 	•	responseTimeout: 클라이언트가 서버에 요청을 보낸 후, 서버에서 응답을 보내는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * 	•	readTimeout: 서버에서 응답을 시작했으나, 클라이언트가 응답 데이터를 읽는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * 	•	writeTimeout: 클라이언트가 서버에 데이터를 보내는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * */
        // HttpClient 구성
        HttpClient httpClient = HttpClient.create()
                // 연결 타임아웃 설정 (5000ms)
                .responseTimeout(Duration.ofMillis(5000)) // 응답 대기 시간
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃
                // 이 핸들러는 서버로부터 응답을 읽을 때 지정된 시간(밀리초) 동안 응답이 오지 않으면 타임아웃을 발생시킵니다.
                .doOnConnected(conn -> conn
                        //  1.	읽기 타임아웃: 서버에서 응답을 받는 데 5초를 초과하면 타임아웃 에러 발생
                        //	2.	쓰기 타임아웃: 서버로 데이터를 전송하는 데 5초를 초과하면 타임아웃 에러 발생
                        .addHandlerLast(new ReadTimeoutHandler(5000))  // 읽기 타임아웃
                        .addHandlerLast(new WriteTimeoutHandler(5000)));

        return WebClient.builder()
                .baseUrl("http://localhost:3000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // ExchangeStrategies를 설정하여 메모리 크기 제한 설정 (16MB)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
                                                            //.maxInMemorySize(-1)) //제한없음
                        ) // 최대 메모리 크기 설정 (16MB)
                        .build())
                .build();
    }
}
````

### Service
```properties
# ✅ Interface는 제외하고 진행
#    - 간단한 응답을 하는 Response Server는 Node(express) 서버 사용
#    - 비동기, 동기식의 구분은 block(); 함수 사용 여부와 대부분의 코드가 비슷하기에 비동기식 코드에 중점적으로 작성함
```

#### 1 ) 비동기식
```properties
# ✅ WebClient 객체는 체이닝 형식으로 이뤄져 있음
```

#### 1 - 1 ) 주요 메서드
- `onStatus()` : Response Status 값을 통해 예외 처리 가능
  - 해당 **발생 예외**를 **Client에게 전파**하기 위해서는 `onErrorResume()`를 사용해서 `Mono.just()` 통해 **값 전달 필요**
- `bodyToMono(타겟.class)` : 응답 값을 매핑할 Class 지정
  - String.class를 이용하면 값을 그대로 전달 가능하고 DTO 매핑의 경우 **상황에 따라 유동적 처리가 중요**
- ##### 👉 에러 처리 방법 ( 택 1 필요 )
  - `onErrorMap()` : 사용하면 발생한 에러를 **다른 유형의 에러로 변환**하여 반환
  - `onErrorResume()` : **대체 데이터를 반환**하거나 다른 흐름으로 복구
  - `doOnError()` : 에러가 발생했을 때만 **특정 작업을 수행**
- `doOnTerminate()` : 무조건 실행  

#### 1 - 2 ) Method별 처리 및 파라미터 전달 방법
- PathVariable
  - `uri("/path/{variable}", variable)`
- Get Type Query Param
  ```javascript
  webClient.get()
     .uri(uriBuilder -> uriBuilder.path("/query")
     .queryParam("param", param)
     .build())
  ```
- Post JSON Body
  ```javascript
    webClient
      .post()
      .uri("/process")
      .bodyValue(SampleDTO.builder.data(data).build())
      .retrieve()
      .bodyToMono(String.class)
      .doOnTerminate(() -> log.info("Request to /process completed"));
    ```

- Post Multipart
    ```javascript
    // MultipartBodyBuilder 대신 MultiValueMap 사용 가능
    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

    // MultipartFile을 body에 추가
    bodyMap.add("file", fileData.getResource()); // fileData 자체를 사용
    bodyMap.add("additionalData", name); // additionalData는 단순 String

    return webClient
                .post()
                .uri("/upload")
                .bodyValue(bodyMap)  // 멀티파트 본문 전송
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /upload completed"))
                // onStatus에서 발생한 에러를 해당 onErrorResume에서 캐치 후 처리할 수 있음
                .onErrorResume(e -> {
                    log.error("Error occurred: {}", e.getMessage());
                    return Mono.just(e.getMessage()); // Return the error message as a string
                })
    ```

#### 1 - 3 ) 전체 코드

```java
@Service
@RequiredArgsConstructor
@Log4j2
public class AsyncSampleServiceImpl {

    private final WebClient webClient;

    public Mono<ResDTO> getPathVariable(String variable) {
        return webClient
                .get()
                .uri("/path/{variable}", variable)
                .retrieve()
                /**
                 * ✅ 상태코드에 맞는 처리 -> 받은 값 그대로 넘겨줌
                 *     - 해당 상태값에 맞는 처리리된 데이터는 예외로 변경되어 던지지고 하단 에러 처리 부분에 원하는 방식으로 차리가 가능하다.
                 * */
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(ResDTO.class)
                /**
                 * ✅ 에러 처리 방법 : 순차적 실행 원하는 처리 방법만 골라 지정 필요
                 *    - onErrorMap
                 *    - onErrorResume
                 *    - doOnError
                 * */
                // 사용하면 발생한 에러를 다른 유형의 에러로 변환하여 반환
                .onErrorMap(error -> {
                    log.error("Mapping error for /path: {}", error.getMessage());
                    return new RuntimeException("Custom Exception: " + error.getMessage());
                })
                // 에러 발생 시 대체 데이터를 반환하거나 다른 흐름으로 복구할 수 있습니다.
                .onErrorResume(error -> {
                    log.error("Error during request to /path: {}", error.getMessage());
                    // 대체 데이터를 반환
                    return Mono.just(new ResDTO("9999","연계 에러 발생","Error!!"));
                })
                // 에러가 발생했을 때만 특정 작업을 수행
                .doOnError(error -> log.error("Error occurred during request to /path: {}", error.getMessage()))
                /**
                 * ✅ 무조건 처리되는 메서드
                 *    - doOnTerminate
                 * */
                // 스트림이 어떻게 종료되었는지에 상관없이 무조건 실행되는 종료 시점 처리용 메서드로, 주로 로그 남기기, 리소스 정리, 모니터링 같은 작업에 활용됩니다.
                .doOnTerminate(() -> log.info("Request to /path completed"))

                ;
    }

    public Mono<String> getQueryParam(String param) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/query")
                        .queryParam("param", param)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /query completed"));
    }

    public Mono<String> postProcess(String data) {
        return webClient
                .post()
                .uri("/process")
                //.bodyValue(new DataRequest(data))
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /process completed"));
    }

    public Mono<String> uploadFile(MultipartFile fileData, String name) {

        // MultipartBodyBuilder 대신 MultiValueMap 사용 가능
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

        // MultipartFile을 body에 추가
        bodyMap.add("file", fileData.getResource()); // fileData 자체를 사용
        bodyMap.add("additionalData", name); // additionalData는 단순 String

        return webClient
                .post()
                .uri("/upload")
                .bodyValue(bodyMap)  // 멀티파트 본문 전송
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /upload completed"))
                // onStatus에서 발생한 에러를 해당 onErrorResume에서 캐치 후 처리할 수 있음
                .onErrorResume(e -> {
                    log.error("Error occurred: {}", e.getMessage());
                    return Mono.just(e.getMessage()); // Return the error message as a string
                })
                ;
    }
}
```