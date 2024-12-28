# WebSocket Study

## 사용 방법

### Dependencies 추가

#### build.gradle

```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
}
```
### Custom WebSocket Handler 
- TextWebSocketHandler 상속 받아 구현
  - Override 구현 메서드
    - `void afterConnectionEstablished()` : **소켓 통신 연결** 시 실행될 메서드
    - `void handleTextMessage()` : 소켓을 통해 **메세지 전송** 시 실행될 메서드
    - `void afterConnectionClosed()` : 소켓 **통신 종료** 시 실행될 메서드
- `@Component`를 사용해 **Bean 등록**
- 참고 
  - `List<WebSocketSession> list` :  소켓에 연결된 세션 저장 용도 
    - 실제 운영 환경 시 Redis를 통한 구축 필요
```java
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

    /** Client가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 목록에서 제거
        this.list.remove(session);
        log.info(session + " 클라이언트 접속 해제");
    }

  /**
   * 모든 접속 대상자에게 메세지 전달 
   * @param  message the websocket Message
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

### WebSocketConfig Class
```properties
# ℹ️ 사용 될 WebSocket에 대한 설정 Class
#    - url 지정 및 커스텀된 메서드 class 주입 필요
```
- `@EnableWebSocket`
  - 웹 소켓 사용 선언

```java
@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket을 활성화
public class WebSocketConfig implements WebSocketConfigurer {

  // WebSocket을 컨트롤하기 위하여 주입
  private final WebSocketHandler chatHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(chatHandler     // 핸들러 주입
                    , "ws/chat")        // 사용될 url 설정
            .setAllowedOrigins("*");    // CORS 설정 모두가 접근 가능
  }
}
```
### 모든 상대에게 Message 전달

```properties
# ℹ️ 관리자의 개입이 필요할 때 사용하는 기능이다. 필수 구현 요소가 아님
```

```java
// Constroller

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {
    private final WebSocketHandler chatHandler;
    @GetMapping("/send")
    @ResponseBody
    public ResponseEntity<String> sendClient(String message) throws Exception{
      chatHandler.sendMsgToAllClient(message);
        return ResponseEntity.ok("success");
    }
}
```

#### Client
- `ws://`로 시작하여 웹 소켓 프로토콜임을 지정
- Path 부분은 WebSocketConfig에서 설정한 동일한 Path 사용 "ws/chat`
- `websocket.onmessage` 부분이 **핵심 기능**

```javascript
// websocket객체 생성
const websocket = new WebSocket("ws://localhost:8080/ws/chat");

// 메세지 전송 버튼
document.querySelector("#button-send").addEventListener("click",()=>{
    send();
})

/**
* Send Message Function
*/
const send = ()=>{
    // text Input
    const msg = document.getElementById("msg");
    // 소켓을 통해 전달
    websocket.send(`${username} : ${msg.value}`);
    // 초기화
    msg.value = '';
}

/**
* Exit WebSocket Function
*/
const onClose = (evt) =>{
    websocket.send(`${username} 님이 방을 나가셨습니다."`);
}

/**
* Join WebSocket Function
*/
const onOpen = (evt) => {
    websocket.send(`${username} 님이 입장하셨습니다."`);
}

/**
* Server <-> Client 상호 작용 Function
* @param msg : Server 전달 받은 데이터
*/
const onMessage = (msg) => {

    // 현재 Client의 아이디를 지정핸다.
    const cur_session = "흑곰";

   // 소켓통신 Server 전달 받은 데이터
    const data = msg.data;
    // Data Split - 상황에 맞게 처리
    const arr = data.split(":");
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
 *  WebSocket 실행 Method Setting
*/
// 👉 Server <-> Client  상호간 수신된 메시지를 처리하는 역할을 수행 
websocket.onmessage = onMessage;
// WebSocket 연결 Function Matching
websocket.onopen    = onOpen;
// WebSocket 종료 Function Matching
websocket.onclose   = onClose;
```

## SocketJS 

### SocketJS란?
- 브라우저에서 WebSocket을 지원하지 않는 경우에 해결 대안 및 **자동 재접속** 기능 제공
-  Spring에서는 `@EnableWebSocket`을 사용하여 WebSocket을 설정
  - SockJS를 **WebSocket 대체 방식으로 사용**할 수 있음


## SocketJS 주요 특징

1. **WebSocket 대체**: WebSocket을 지원하지 않는 환경에서 WebSocket처럼 동작하여 실시간 통신을 가능하게 함

2. **다양한 폴백 옵션**: WebSocket이 불가능할 경우 자동으로 HTTP 기반의 다른 방식(예: AJAX Long Polling)을 사용하여 통신을 유지합니다.

3. **양방향 통신**: 클라이언트와 서버 간 실시간 양방향 메시징을 지원

4. **호환성**: 다양한 브라우저와 네트워크 환경에서 안정적으로 동작

5. **서버 측 지원**: 서버에서도 SocketJS를 통해 클라이언트와의 실시간 연결을 설정하고 메시지를 주고받을 수 있음
   - `node.js`에서는 `Socket.io`를 사용하는 것이 일반적이다.
   - `Spring`에서는 `SockJS`를 사용하는것이 일반적이다.

### 적용 방법

#### WebSocketConfig Class
- SocketJS 적용 시 보안상 문제로 `setAllowedOrigins("*")` 방식 사용 불가능
  - `setAllowedOriginPatterns()` 방식으로 변경 필요
```java
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

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

#### Client
- SocketJS Import 필수
- 기존 WebSocket 객체 생성 방법 변경
  - `new WebSocket` -> `new SocketJS()` 생성 객체 방법 변경
```javascript
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>

/***
 *   ✅ WebSocket가 아닌 SockJS를 사용하여 기동함
 *   
 *   첫번째 인자 : Socket 서버의 URL
 *   두번째 인자 : 일반적인 사용 시에는 null로 설정하면 됩니다. 이 매개변수는 SockJS 클라이언트의 동작에 대한 옵션을 제공할 수 있습니다.
 *   세번째 인자 : SockJS 클라이언트의 전송 방식(transport)을 지정하는 옵션입니다.
*/
const websocket = new SockJS("/ws/chat", null, {transports: ["websocket", "xhr-streaming", "xhr-polling"]});

// 하위 코드는 같음
```

### STOMP

#### STOMP란?

-  웹 소켓(WebSocket)과 함께 자주 사용되는데, 웹 소켓은 양방향 실시간 통신을 제공하고 STOMP는 메시징 시스템과의 상호 작용을 담당합니다.
-  STOMP 프로토콜은 WebSocket 위에서 동작하는 프로토콜로써 클라이언트와 서버가 전송할 메세지의 유형, 형식, 내용들을 정의하는 매커니즘이다.
-  STOMP를 이용하면 메세지의 헤더에 값을 줄 수 있어 헤더 값을 기반으로 통신 시 인증 처리를 구현하는 것도 가능하며 STOMP 스펙에 정의한 규칙만 잘 지키면 여러 언어 및 플랫폼 간 메세지를 상호 운영할 수 있다 
  - STOMP는 Text 지향 프로토콜이나, Message Payload에는 `Text or Binary` 데이터를 포함 할 수 있다.
- 메세징 전송을 효율적으로 하기 위해 탄생한 프로토콜이고, 기본적으로 `pub / sub 구조`이다. 메세지를 전송하고 메세지를 받아 처리하는 부분이 확실히 정해져 있기 때문에 개발자 입장에서 명확하게 인지하고 개발할 수 있는 이점이 있다.




#### 사용 이유
- 간편하게 하나의 소켓이 아닌 여러개의 소캣을 나눠서 사용할 수 있다.

#### 사용 방법

- 1 . stomp-websocket  Dependencies에 추가
```groovy
// build.gradle

// code..

dependencies {
  //  tarter-websocket
  implementation 'org.springframework.boot:spring-boot-starter-websocket'

  // STOMP 추가
  // https://mvnrepository.com/artifact/org.webjars/stomp-websocket
  implementation group: 'org.webjars', name: 'stomp-websocket', version: '2.3.4'

}
```

- 2 . 사용을 위한 Config class 작성
  -  중요 포인트
    - 소켓 통신 전 HandShake용 Path 지정이 필요하다.
    - 보안 상 문제로 "*" Origin 설정이 불가능하닫.
    - 요청,응답 path 설정을 해준다 `/pub`, `/sub`
```java
// java StompWebSocketConfig - WebSocketMessageBrokerConfigurer 구현함

@Configuration
@EnableWebSocketMessageBroker // 👉 Stomp를 사용하기위해 추가
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    //WebSocket 또는 SockJS Client가 웹소켓을 사용하기 위해 "핸드셰이크" 커넥션을 생성할 Path
    registry.addEndpoint("/stomp/chat")
            // "*" 적용 시 보안상 문제로 에러 발생
            .setAllowedOrigins("http://localhost:8080", "http://localhost:8081")
            .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // Client에서 SEND 요청을 처리 - 요청 path 시작 설정
    registry.setApplicationDestinationPrefixes("/pub");
    //  해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행 - 응답 path 시작 설정
    registry.enableSimpleBroker("/sub");
  }
}
```

- 3 . Client와 값전달을 편하게 하기위한 DTO 작성
```java
// ChatRoomDTO

@Getter
@Setter
public class ChatRoomDTO {

  // 소켓을 구분하기 위한 Id 사용
  private String roomId;

  // 해당 방의 이름 구분
  private String name;

  //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션 - 예제에서는 실직적 사용 ❌
  private Set<WebSocketSession> sessions = new HashSet<>();

  // 객체 생성 시 방의 이름을 주입 받고 아이디는 UUID로 생성
  public ChatRoomDTO (String name){
    this.roomId = UUID.randomUUID().toString();
    this.name = name;
  }

}
```

- 4 . STOMP 통신을 받을 Controller 작성
  - Client에서 메세지 전송 시 해당 `path를 읽고 작동`
  - simpMessagingTemplate.convertAndSend(`"path"`, `"소켓이 구분될 아이디"`, `전달 메세지`)
    - 서버에서 만약 해당 소켓으로 전달하려면 `Message를 작성`해서 전달하면 된다.
```java

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * @MessageMapping 을 통해 WebSocket으로 들어오는 메세지 발행을 처리한다.
     *
     * - 둘의 URL Path 정보를 구분해 놓은 이유는
     *   enter의 경우 Client에서 첫 한번만 실행 되게 끔 함 "~님이 입장"을 위해 사용되었음
     *   message의 경우 받아온 메세지를 전달 하기 위함임
     * **/
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
```


- 6 . 채팅방을 생성하기 위한 로직
  - DB를 통해 사용해야하나 예제이기 떄문에 Memory를 사용하여 구현함.
  - 간단한 로직이기에 주석으로 내부 코드를 설명함

```java
// ChatRoom 생성 Controller

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Log4j2
public class RoomController {

  private final ChatRoomRepository chatRoomRepository;
  
  //채팅방 목록 조회
  @GetMapping("/roomList")
  @ResponseBody
  public ResponseEntity<List<ChatRoomDTO>> rooms(){
    return ResponseEntity.ok(chatRoomRepository.findAllRooms());
  }

  //채팅방 개설
  @PostMapping("/room")
  @ResponseBody
  public String create(@RequestBody String name){
    log.info("# Create Chat Room , name: " + name);
    chatRoomRepository.createChatRoomDTO(name);
    return "success";
  }

}


//--------------------------------------------------------------------------------------


// ChatRoomRepository

@Repository
public class ChatRoomRepository {

  private Map<String, ChatRoomDTO> chatRoomDTOMap;
  
  // 👉 Memory를 사용하기에 객체 LinkedHshMap으로 객체 생성
  @PostConstruct
  private void init(){
    chatRoomDTOMap = new LinkedHashMap<>();
  }

  // 👉 모든 방 찾기
  public List<ChatRoomDTO> findAllRooms(){
    //채팅방 생성 순서 최근 순으로 반환
    List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
    Collections.reverse(result);
    return result;
  }


  // 👉 채팅 방 생성 - 방이름을 파라미토로 받고 UUID를 통해 ChatRoomDTO객체 생성
  public ChatRoomDTO createChatRoomDTO(String name){
    ChatRoomDTO room = new ChatRoomDTO(name);
    chatRoomDTOMap.put(room.getRoomId(), room);
    return room;
  }

}
```

- 7 . Client 사용
  - `Socket.js, Stomp.js` import는 **필수**이다
  - 방에 입장하기 위해서는 해당 방의 ID값을 전달해줘야 함
    - Socket 연결중 어떠한 연결을 할지 정하기 위함
  - `stomp.debug = null` 설정을 해주지 않으면 pub, sub 시 로그가 계속 찍힘

```html
<!--  client -->

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
    // 편의상 Get방식을 사용함
    const urlParams = new URL(location.href).searchParams;
    const roomName  = urlParams.get('roomName');
    const roomId    = urlParams.get('roomId');
    const username  = "8080포트";

    /**
    * 1. SockJS를 객체를 사용하여 Stomp객체 생성
        - 상대경로 작성 시 다른 클라이언트에서는 해당 포트가 적용 되므로 Full Path를 사용하자
    */
    const stomp = Stomp.over( new SockJS("http://localhost:8080/stomp/chat"));

    // 👉 해당 설정을 해줘야 sub, pub 시 로그기 안나옴
    stomp.debug = null;

    //2. connection이 맺어지면 실행 
    stomp.connect({}, () => {

       //4. subscribe(path, callback)으로 메세지를 받을 수 있음 - pub로 데이터가 들어올 경우 자동 실행
       // 👉 StompWebSocketConfig.java에서 설정한 구독(응답 시) '/sub'을 사용함
       stomp.subscribe("/sub/chat/room/" + roomId, function (chat) {
           const content = JSON.parse(chat.body);
           const writer = content.writer;
           const str = `<div class='col-6'>
                        <div class='alert ${writer === username ?"alert-secondary" :  "alert-warning"}'>
                          <b> ${writer} : ${content.message} </b>
                          </div>
                      </div>`;
           document.querySelector("#msgArea").insertAdjacentHTML("beforeEnd",str);
       });

       //3. send(path, header, message)로 메세지를 보낼 수 있음 - 최초 1회 실행 시킴
       stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, writer: username}))
    });

    // 메세지 전송 버튼 이벤트
    document.querySelector("#button-send").addEventListener("click", (e)=> {
        const msg = document.getElementById("msg");
        // 👉 StompWebSocketConfig.java에서 설정한 구독(요청 시) '/pub'을 사용함
        stomp.send('/pub/chat/message', {}, JSON.stringify({roomId: roomId, message: msg.value, writer: username}));
        msg.value = '';
    });
</script>
```
