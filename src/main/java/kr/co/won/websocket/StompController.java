package kr.co.won.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

/**
 * STOMP controller
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

    /** Message publisher */
    private  final SimpMessagingTemplate messagingTemplate;

    @MessageMapping(value = {"/stomp/*"})
    @SendTo(value = {"/stomp/testing"})
    public String testController(WebSocketSession session, String message) {

        return null;
    }

    @MessageMapping(value = "/test")
    public String test(String msg) {
        log.info("testing message controller");
        log.info("message controller get socket message :::: {}", msg);
        messagingTemplate.convertAndSend("/topic/iot/tester", "Testing");
        return "testingDone";
    }
}
