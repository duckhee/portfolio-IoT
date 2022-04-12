package kr.co.won.websocket;


import kr.co.won.auth.JwtToken;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = {
        RestDocsConfiguration.class,
        JwtToken.class,
        UserFactory.class
})
public class SocketInitTests {

    // get server port
    @LocalServerPort
    private Integer serverPort;

    @Autowired
    private PasswordEncoder passwordEncoder = new TestAppConfiguration().passwordEncoder();

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    // test socket client
    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;


    @Autowired
    private UserPersistence userPersistence;


    @BeforeEach
    void setup() {
        userPersistence.deleteAll();
        // make client socket
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(
                new SockJsClient(Arrays.asList(
                        new WebSocketTransport(
                                new StandardWebSocketClient()
                        ))
                ));
    }



    @DisplayName(value = "00. stomp socket login not validation Failed Tests")
    @Test
    void connectionFailedByInvalidateTokenTest() {

        // given
        StompHeaders headers = new StompHeaders(); // 헤더에 토큰 값 삽입
        headers.add(HttpHeaders.AUTHORIZATION, "invalidate token");

        // when, then
        // 잘못된 토큰으로 연결하면 예외 발생
        assertThatThrownBy(() -> stompClient
                .connect(getStompPath(), new WebSocketHttpHeaders(), headers, new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS)).isInstanceOf(ExecutionException.class);
    }

    @DisplayName(value = "00. stomp socket subscript and publish Tests")
    @Test
    void subscriptMsgTest() throws ExecutionException, InterruptedException, TimeoutException, IOException {

        // given
        StompHeaders headers = new StompHeaders(); // 헤더에 토큰 값 삽입
        String jwtSocketToken = jwtToken.getSocketToken(UserRoleType.USER, "tester", "tester");
        headers.add(HttpHeaders.AUTHORIZATION, jwtSocketToken);
        // get login and connection
        StompSession connectionSession = stompClient.connect(getStompPath(), new WebSocketHttpHeaders(), headers, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
        log.info("get stomp connection session ::: {}", connectionSession.toString());
        // connection subscript setting
        connectionSession.subscribe("/topic/alarm/tester", new DefaultStompFrameHandler());
//        connectionSession.subscribe("/test", new DefaultStompFrameHandler());
        // send msg
        connectionSession.send("/test", "test".getBytes(StandardCharsets.UTF_8));
        String jsonResult = blockingQueue.poll(1, SECONDS);
        log.info("result ::: {}", jsonResult);
        Assertions.assertThat(jsonResult).isEqualTo("Testing");
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            log.info("get stomp headers ::: {}", stompHeaders.toSingleValueMap());
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            log.info("get stomp header ::: {}, Object ::: {}", stompHeaders.toString(), o.toString());
            blockingQueue.offer(new String((byte[]) o));
        }
    }

    //get stomp path default path
    private String getStompPath() {
        return String.format("ws://localhost:%d/socket/stomp", serverPort);
    }

}
