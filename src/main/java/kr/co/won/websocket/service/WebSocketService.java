package kr.co.won.websocket.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * File Name        : WebSocketService
 * Author           : Doukhee Won
 * Date             : 2022/03/17
 * Version          : v0.0.1
 * <p>
 * Web Socket Service Layer
 */

public interface WebSocketService {


    public default void chat(WebSocketSession socketSession, TextMessage message) {
        return;
    }

}
