package kr.co.won.websocket;

import kr.co.won.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;

/**
 * SocketJs message handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketJsHandler extends TextWebSocketHandler {


    // get user
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("get socket ::: {}", session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LoginUser principal = (LoginUser) session.getPrincipal();
        log.info("principal user ::: {}", principal);
        log.info("get message ::: {}", message);
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("socket close ::: {}, status ::: {}", session, status);
        super.afterConnectionClosed(session, status);
    }
}
