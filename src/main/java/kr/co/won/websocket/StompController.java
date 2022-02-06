package kr.co.won.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

@Controller
@RequiredArgsConstructor
public class StompController {

    @MessageMapping(value = {"/stomp"})
    @SendTo(value = {"/sockets/testing"})
    public String testController(WebSocketSession session, String message) {

        return null;
    }
}
