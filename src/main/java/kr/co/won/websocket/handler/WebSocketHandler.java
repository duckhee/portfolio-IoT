package kr.co.won.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;

/**
 * File Name        : WebSocketHandler
 * Author           : Doukhee Won
 * Date             : 2022/03/17
 * Version          : v0.0.1
 * <p>
 * Web Socket message handler
 * pure web socket using this handler
 * WebSocketSession using database redis
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {


    // socket connection
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("get connect web socket");
        log.info("get socket ::: {}", session);
    }

    // socket text message
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        LoginUser principal = (LoginUser) session.getPrincipal();
//        log.info("principal user ::: {}", principal);
        log.info("get web socket session ::: {}", session);
        log.info("get message ::: {}", message);
        log.info("get url ::: {}", session.getUri());
        log.info("get url path ::: {}", session.getUri().getPath());
        
        if (session.getAttributes() != null) {
            log.info("get principal :::: {}", session.getAttributes());
        }
        // send message
        TextMessage sendMsg = new TextMessage("server get :::: " + message.getPayload());
        session.sendMessage(sendMsg);
    }

    // socket close
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("web socket close");
        log.info("socket close ::: {}, status ::: {}", session, status);
    }


}
