package kr.co.won.chat.controller.stomp;

import kr.co.won.chat.form.MessageForm;
import kr.co.won.chat.persistence.ChattingRoomPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingStompController {
    // Broker sender
    private final SimpMessagingTemplate template;
    // get chatting room
    private final ChattingRoomPersistence chatRoomPersistence;

    /**
     * chatting room enter
     */
    @MessageMapping(value = "/{roomId}/enter")
    public void enter(@DestinationVariable(value = "roomId") String roomId, MessageForm message) {
        // get room id
        log.info("get room ID ::: {}", roomId);
        // log enter new User Message
        log.info("get enter msg ::: {}", message);
        message.setMessage(message.getWriter() + " user enter chatting room.");
        String getRoomId = message.getRoomId();
        // send chatting room enter user message
        // send msg chatting room in user
        MessageForm sendEnterMsg = MessageForm.builder()
                .roomId(getRoomId)
                .message(message.getWriter() + "user enter")
                .build();
        template.convertAndSend("/topic/chat/room/" + getRoomId + "/enter", message);
    }

    /**
     * chatting room chat subscription
     */
    @MessageMapping(value = "/chat/room")
    public void message(MessageForm message) {
        log.info("send msg ::: {}", message);
        template.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
