package kr.co.won.websocket.stomp.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * STOMP controller
 */
@Controller
@RequiredArgsConstructor
public class ChattingStompController {

    // send message
    private final SimpMessagingTemplate msgTemplate;

    @MessageMapping(value = "/stomp/chat/enter")
    public void enterChattingRoom(){
    }

    @MessageMapping(value = "stomp/chat/msg")
    public void chattingMessage(){

    }
}
