package kr.co.won.websocket.stomp.controller;


import kr.co.won.websocket.stomp.form.ChatMsgForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * STOMP controller
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingStompControllers {

    // send message
    private final SimpMessagingTemplate msgTemplate;

    @MessageMapping(value = "/room/enter")
    @SendTo(value = "/stomp/chat/enter")
    public void enterChattingRoom() {
    }

    @MessageMapping(value = "room/{roomId}/msg")
    public void chattingMessage(@DestinationVariable(value = "roomId") String roomId, ChatMsgForm msg) {
        log.info("get room Number ::: {}, chatting msg form ::: {}", roomId, msg);
    }
}
