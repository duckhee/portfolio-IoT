package kr.co.won.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketJsHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("socketIo connected");
        log.info("get socket js connect :::: {}", session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("socketIo handle Message");
        log.info("get socket js get Message :::: {} = {}", session, message.toString());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("socketIo handle Text Message");
        log.info("get socket js get Message :::: {} = {}", session, message.toString());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("socketIo handle Pong Message");
        log.info("get socket js get Message :::: {} = {}", session, message.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("socketIo error");
        log.info("get socketIo session :::: {}, exception ::: {}", session, exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("socketIo close");
        log.info("socket close ::: {}, status ::: {}", session, status);
    }
}
