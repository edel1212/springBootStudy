# WebSocket Study

### WebSocket이란?

- 기존의 단방향 HTTP 프로토콜과 호환되어 양방향 통신을 제공하기 위해 개발된 프로토콜이다.
  - 기존 HTTP 프로토콜 통신은 클라이언트가 먼저 요청을 보내고, 그 요청에 응답한 후 끝나는 방식이다.
    - 이러한 방식은 주기적으로 서버와 데이터를 주고받고 업데이트를 해야하는 상황에는 굉장히 효율이 떨어질 수 밖에 없다.
- HTTP의 통신 포트는 80번, HTTPS는 443번이다. 따라서 방화벽에 제약이 없다.
  - HTTP Request를 그대로 사용하기 떄문에 CORS 적용이나 JWT 인증등을 기존과 동일하게 적용이 가능하다.
- 접속까지는 HTTP 프로토콜을 이용하고 그 이후 통신은 자체적은 WebSocket 프로토콜 통신하게 된다.
- 클라이언트가 요청을 하고 웹서버가 응답한 후 연결을 끊는 방식이 아닌 `Connection`을 그대로 **유지 하고** 클라이언트의 요청 없이도  
상호간의 데이터를 전송할수 있는 프로토콜이다.
- 프로토콜의 요청은 `ws://도메인주소:포트번호`로 이루워 진다.

<br/>
<hr/>

### 언제 사용하는것이 좋을까 ?
웹소켓은 서비스를 동적으로 만들어 주지만, Ajax, Streaming, Long polling 기술이 더 효과적일 경우도 있다. 예를 들어, ***변경 사항의 빈도가 자주 일어나지 않고,   
데이터의 크기가 작은 경우 Ajax, Streaming, Long polling 기술이 더 효과적일 수 있다.*** 

**즉, 실시간성을 보장해야 하고, 변경 사항의 빈도가 잦거나 또는 짧은 대기 시간, 고주파수, 대용량의 조합인 경우 WebSocket이 좋은 해결책이 될 수 있다.**  



뉴스나 메일, SNS 피드는 동적으로 업데이트 하는 것은 맞지만 몇 분마다 업데이트 하는 것이 좋다. 반면 협업, 게임, 금융 앱은 훨씬 더 실시간에 근접해야한다.

<br/>
<hr/>

### WebSocket 과 Socket 비교

#### 정의
- WebSocket
  - 하나의 TCP 접속에 전이중 통신 채널을 제공하는 컴퓨터 통신 프로토콜이다.
  - HTTP 나 HTTPS 위에서 동작하도록 설계뙤어있다.
  - HTTP 프로토콜과 구별은 되지만 호환이 가능하다.
- Socket
  - 네트워크상에서 동작하는 프로그램 간 통신의 종착점 1:1 통신의 경우 양측 다 소켓 통신이 존재해야 통신이 가능하다.
  - 인터넷 프로토콜(TCP,UDP)에 기반하고 있으므로 대부분의 네트워크 소켓은 인터넷 소켓이다.

#### 차이점
- 동작계층
  - [WebSocket] : TCP에 의존하지만 HTTP에 기반하므로 7계층에 위치한다.
  - [Socket] : OSI 7계층 기준으로 소켓은 인터넷 프로토콜에 기반하므로 TCP, UDP가 속한 4계층에 위치한다.
- 데이터 형식
  - [WebSocket] : 어플리케이션 계층인 7계층에 기반하기 때문에 메시지 형식의 데이터를 다루게 된다.
  - [Socket] : TCP에 기반한 소켓 통신은 단순히 바이트 스트림을 통한 데이터 전송이므로 바이트로 이루어진 데이터를 다룸

#### 결론
- 소켓과 웹 소켓은 서로 상반된 개념이 아니다 웹소켓은 TCP 소켓과 구분되는 것이 아니라 TCP 소켓의 추상호ㅘ된 형태라 보자!
- 소켓 통신에 기반하여 웹소켓은 웹어플리케이션에 맞게 ㅂ라전한 형태로 소켓 통신을 하는것으로 이해하자


<br/>
<hr/>

### 적용방법

- 1 . Dependencies 추가
```groovy
// build.gradle

dependencies {
    // Add WebSocket 
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
}
```

- 2 . WebSocket Handler 구현
  - TextWebSocketHandler를 상속 받아 구현
    - Override 구현 메서드
      - void handleTextMessage() : 소켓을 통해 메세지 `전송` 시 실행될 메서드
      - void afterConnectionEstablished() : 소켓 통신 `연결` 시 실행될 메서드
      - void afterConnectionClosed() : 소켓 통신 `종료` 시 실행될 메서드
  - ✅ 중요 ! :  `List<WebSocketSession> list`에는 소켓에 연결된 세션을 관리하기 위해 사용한다! 
```java
// WebSocketHandler - TextWebSocketHandler 상속 구현

@Component
@Log4j2
public class WebSocketHandler extends TextWebSocketHandler {

    /**
     * WebSocket 세션 목록(list)에 있는 모든 세션에게 메시지를 보내기 위해 사용됩니다.
     *
     * 2개의 클라이언트 연결 시 들어있는 목록
     *  - [StandardWebSocketSession[id=8feb5e84-0399-b91a-e334-3526ed284250, uri=ws://localhost:8080/ws/chat]
     *  - , StandardWebSocketSession[id=e9ba4e10-747b-bdae-7466-e24b738be127, uri=ws://localhost:8080/ws/chat]]
     * */
    private  static List<WebSocketSession> list = new ArrayList<>();

    /** 통신간 메서드 전송시 사용될 메서드 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 연결된 모든 세션에 전달하기 위한 Loop
        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /** Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결 목록에 추가
        this.list.add(session);
        log.info(session + " 클라이언트 접속");
    }

    /** Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 목록에서 제거
        this.list.remove(session);
        log.info(session + " 클라이언트 접속 해제");
    }
}
```

- 3 . WebSocketConfig 구현
  - WebSocketConfigurer를 Implements 하여 `registerWebSocketHandlers();` 구현함
  - 위에서 작성한 `WebSocketHandler` DI 주입
  - WebSocket을 설정해줌

```java
// WebSocket 구현 -  WebSocketConfigurer 인터페이스 구현

@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket을 활성화
public class WebSocketConfig implements WebSocketConfigurer {

  // WebSocket을 컨트롤하기 위하여 주입
  private final WebSocketHandler chatHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(chatHandler         // 핸들러 주입
                    , "ws/chat")    // 사용될 url 설정
            .setAllowedOrigins("*");        // CORS 설정 모두가 접근 가능
  }
}
```

- 4 . WebSocket 사용
  - 소켓 연결시 Protocol 부분은 `ws://`로 시작하며 Path 부분은 WebSocketConfig에서 설정한 값이다 `"ws/chat"`로 설정함
  - 아래 코드중 마지막 Line의 `WebSocket 통신 설정`이 가장 중요하다.
    - onMessage는 무조건 실행되는것이 포인트이다.

```html
<!-- 클라이언트 소켓 연결  -->

<script>
    // websocket객체 생성
    const websocket = new WebSocket("ws://localhost:8080/ws/chat");

    // 하드코딩 유저네임
    const username = "8081님";

    // 전송 버튼 클릭 시
    document.querySelector("#button-send").addEventListener("click",()=>{
        send();
    })

    // 메세지 전송!!!
    const send = ()=>{
        // text Input
        const msg = document.getElementById("msg");
        // 소켓을 통해 전달
        websocket.send(`${username} : ${msg.value}`);
        // 초기화
        msg.value = '';
    }

    //채팅창에서 나갔을 때
    const onClose = (evt) =>{
        websocket.send(`${username} 님이 방을 나가셨습니다."`);
    }

    //채팅창에 들어왔을 때
    const onOpen = (evt) => {
        websocket.send(`${username} 님이 입장하셨습니다."`);
    }

    // WebSocket에서 메시지를 수신할 때 실행될 함수
    const onMessage = (msg) => {
        // 소켓통신에서 전달 받은 데이터
        const data = msg.data;
        // 받아온 데이터를 : 기준으로 잘라서 사용 - 이건 상황에 맞게 사용하면 좋을 듯함
        const arr = data.split(":");

        // 현재 Client의 아이디를 지정핸다.
        const cur_session = username;


        // ":"를 기준으로 나눠서 아이디를 가져옴
        const sessionId = arr[0].trim();
        // ":"를 기준으로 나눠서 내용을 가져옴
        const message   = arr[1];


        console.log("sessionID      : " + sessionId);
        console.log("cur_session    : " + cur_session);


        // 내가 쓴글과 상대방이 쓴글을 나눠서 제공 - sessionId == cur_session
        const str = `<div class='col-6'>
                        <div class='alert ${sessionId == cur_session ? "alert-secondary" : "alert-warning'" }'>
                            <b> ${sessionId} :  ${message}</b>
                        </div>
                    </div>`
        document.querySelector("#msgArea").insertAdjacentHTML("beforeEnd",str);
    }

    /**
     *  WebSocket 통신 설정
    */
    // 👉 WebSocket 서버로부터 수신된 메시지를 처리하는 역할을 수행 - 데이터 전송 및 전달 시 ✅ 무조건 실행 !! 중요
    websocket.onmessage = onMessage;
    // WebSocket 연결이 열릴 때 실행될 함수를 지정
    websocket.onopen    = onOpen;
    // WebSocket 연결이 닫힐 때 실행
    websocket.onclose   = onClose;
</script>
```


<br/>
<hr/>

### Server -> Client로 데이터 전달

- 1 . `WebSocketHandler` 내 데이터를 전달해줄 메서드를 생성해준다.
  -  해당 소켓에 연결된 모든 소켓에 전달해야하기 떄문에 소켓을 관리하고 있는 WebSocket Handler에서 구현
    - TextMessage를 사용하여 전달한 데이터를 정의함 
```java
// WebSocketHandler - TextWebSocketHandler 상속 구현

@Component
@Log4j2
public class WebSocketHandler extends TextWebSocketHandler {
    
    // Code ...
    
    /**
     * @description : Service에서 요청하여 소켓에 연결 중인 모든 클라이언트에 값 전달.
     * @param string message
     * @return void
     * **/
    public void sendMessageToAllClient(String message) throws Exception{
      TextMessage textMessage  = new TextMessage(message);
      log.info("------------------------");
      log.info("소켓에 접속중인 세션 목록 :::{}",list);
      log.info("textMessage :::{}",textMessage);
      log.info("------------------------");
    
      // 모든 세션에 전달하기위한 Loop
      for(WebSocketSession sess: list) {
        sess.sendMessage(textMessage);
      }
    }
}
```

- 2 . `sendMessageToAllClient(String str)`을 요청할 Service 생성

```java
// Service

public interface SendClientMsgToSocketService {
  void sendMsgToAllClient(String msg) throws Exception;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ServiceImplements

@Service
@Log4j2
@RequiredArgsConstructor
public class SendClientMsgToSocketServiceImpl implements SendClientMsgToSocketService{
  // DI 주입
  private final WebSocketHandler chatHandler;

  @Override
  public void sendMsgToAllClient(String msg) throws Exception{
    log.info("msg ::::::::{}",msg);
    chatHandler.sendMessageToAllClient(msg);
  }
}
```

- 3 . 사용
  - 현재는 Controller 요청으로 사용하였으나 필요한 부분에서 요청하여 사용 하는방식으로 사용가능

```java
// Constroller

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {
    // DI 주입
    private final SendClientMsgToSocketService sendClientMsgToSocketService;

    @GetMapping("/send")
    @ResponseBody
    public String sendClient() throws Exception{
        sendClientMsgToSocketService.sendMsgToAllClient("서버단에서 전송한것임");
        return "success";
    }
}
```

#### 주의사항
- 문제 
  - 해당 Service를 구현 후 JUnit 테스트시 연결된 클라이언트에 값이 찍히지 않는 문제가 발생하였음 -- 오류는 없었다

- 이유 
  - JUnit에서는 연결되어있는 소켓목록을 가져올 수 없기에 해당 소켓에 값을 전달해도 클라이언트에서 받을 수가 없던것임!
    - `List<WebSocketSession> list`가 비어 있음!
  - 따라서 테스트를 Junit 테스트가 아닌 기동 후 컨트롤러를 연결하여 요청 확인 방식으로 진행 해야한다.


<br/>
<hr/>

### SocketJS 

#### SocketJS란?
- SockJS는 어플리케이션이 WebSocket API를 사용하도록 허용하지만 브라우저에서 WebSocket을 지원하지 않는 경우에 대안으로 어플리케이션의 코드를  
변경할 필요 없이 런타임에 필요할 때 대체하는 것이다.

- 사용 이유
  - 모든 클라이언트가 WebSocket를 지원해준다는 보장이 없기 떄문에 사용한다.
  - Server와 CLient 중간에 위치한 Proxy가 Upgrade 해더를 해석하지 못해 서버에 전달하지 못할 수 있다.
  - proxy가 중간에서 갑자기 connection을 종료하는 경우도 존재한다.
- 해결 방법
  - `WebSocket Emulation`를 사용하는 것이다. (지원하지 않는 클라이언트에서 지원되게 끔 유사한 기능을 모방하는 것)
    - 우선 WebSocket을 시도하고, 실패할 경우 HTTP Streaming, Long-Polling 같은 HTTP 기반의 다른 기술로 전환해 다시 연결을 시도하는 것을 말한다.
    - `node.js`에서는 `Socket.io`를 사용하는 것이 일반적이다.
    - `Spring`에서는 `SockJS`를 사용하는것이 일반적이다.


<br/>

#### SocketJS의 구성
- SocketJS Protocol
- SockJS Javascript Client Library
- SockJS Server


<br/>

#### 전송타입
- WebSocket
  - Handshaking을 위한 하나의 HTTP 요청을 필요로 한다. 이후 연결이 되면 모든 메세지들은 그 이후 사용했던 Socket을 통해 교환된다. 
- HTTP Streaming
  - 서버 -> 클라이언트로의 메세지들을 위해 하나의 Long-running 요청이 있고, 추가적인 HTTP POST 요청은 클라이언트 -> 서버로의 메세지를 위해 사용된다.
- HTTP Long Polling
  - 서버 -> 클라이언트로의 응답 후 현재의 요청을 끝내는 것을 제외하고는 XHR Streaming과 유사하다.

   
<br/>

#### 적용 방법

- 1 . Server에서의 WebSocket 설정 파일에 SocketJS 설정을 추가해준다.
  - SocketJS 적용 시 보안상 문제로 `setAllowedOrigins("*")`는 사용할 수 없다.
  - 사용할 `Origin`을 지정 해줘야햔다.
```java
// WebSocketConfig

package com.yoo.webSocketStudy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket을 활성화
public class WebSocketConfig implements WebSocketConfigurer {

  // WebSocket을 컨트롤하기 위하여 주입
  private final WebSocketHandler chatHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(chatHandler, "/ws/chat")    // 핸들러 주입 및  사용될 uri 지정           
            //.setAllowedOrigins("*")  ❌ SockJS 사용시 보안상 문제로 "*"사용이 불가능해짐 [CORS 설정 ]
            .setAllowedOriginPatterns("http://localhost:8080", "http://localhost:8081") // ✅ Origin 지정
            .withSockJS();                                                              // ✅ SocketJS 추가
  }
}
```

- 2 . Client에서 SocketJS 라이브러리 import 후 기존 사용하던 `new WebSocket` -> `new SocketJS()` 생성 객체 변경
```html
<!-- Client -->

<!-- ✅ SocketJS를 꼭 추가해 줘야한다  --->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
<script>
    // ❌ 변경 const websocket = new WebSocket("ws://localhost:8080/ws/chat");

    /***
     *   ✅ WebSocket가 아닌 SockJS를 사용하여 기동함
     *   
     *   첫번째 인자 : Socket 서버의 URL
     *   두번째 인자 : 일반적인 사용 시에는 null로 설정하면 됩니다. 이 매개변수는 SockJS 클라이언트의 동작에 대한 옵션을 제공할 수 있습니다.
     *   세번째 인자 : SockJS 클라이언트의 전송 방식(transport)을 지정하는 옵션입니다.
    */
    const websocket = new SockJS("/ws/chat", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});
  
    // 하위 코드는 같음
</script>
```

<br/>
<hr/>

### STOMP

#### STOMP란?

-  웹 소켓(WebSocket)과 함께 자주 사용되는데, 웹 소켓은 양방향 실시간 통신을 제공하고 STOMP는 메시징 시스템과의 상호 작용을 담당합니다.
-  STOMP 프로토콜은 WebSocket 위에서 동작하는 프로토콜로써 클라이언트와 서버가 전송할 메세지의 유형, 형식, 내용들을 정의하는 매커니즘이다.
-  STOMP를 이용하면 메세지의 헤더에 값을 줄 수 있어 헤더 값을 기반으로 통신 시 인증 처리를 구현하는 것도 가능하며 STOMP 스펙에 정의한 규칙만 잘 지키면 여러 언어 및 플랫폼 간 메세지를 상호 운영할 수 있다 
  - STOMP는 Text 지향 프로토콜이나, Message Payload에는 `Text or Binary` 데이터를 포함 할 수 있다.
- 메세징 전송을 효율적으로 하기 위해 탄생한 프로토콜이고, 기본적으로 `pub / sub 구조`이다. 메세지를 전송하고 메세지를 받아 처리하는 부분이 확실히 정해져 있기 때문에 개발자 입장에서 명확하게 인지하고 개발할 수 있는 이점이 있다.


#### pub, sub 이란?
- pub(Publish) 발행 : 클라이언트가 메시지를 특정 대상(destination)에 `보내는 동작`을 말합니다.
- sub(Subscribe) 구독 : 클라이언트가 특정 대상(destination)에 대한 `구독을 등록하는 동작`을 의미합니다.
- 이해를 돕기위한 채팅방 예시
  - 채팅방을 생성한다 :: pub / sub 구현을 위한 `Topic이 생성`됨
  - 채팅방 입장 :: Topic `구독`
  - 채팅방에서 메세지 송,수신 :: 해당 `Topic`으로 메세지를 `송신(pub)`, 메세지를 `수신(sub)`

#### 사용 이유
- 간편하게 하나의 소켓이 아닌 여러개의 소캣을 나눠서 사용할 수 있다.