package kr.co.won.chat.controller.stomp;

import kr.co.won.chat.form.MessageForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingStompController {
    // Broker sender
    private final SimpMessagingTemplate template;

    /**
     * chatting room enter
     */
    @MessageMapping(value = "/enter")
    public void enter(MessageForm message) {
        // log enter new User Message
        log.info("get enter msg ::: {}", message);
        message.setMessage(message.getWriter() + " user enter chatting room.");
        // send msg chatting room in user
        template.convertAndSend("/chat/pub/room/" + message.getRoomId(), message);
    }

    /**
     * chatting room chat
     */
    @MessageMapping(value = "/chat/message")
    public void message(MessageForm message) {
        log.info("send msg ::: {}", message);
        template.convertAndSend("/chat/sub/room/" + message.getRoomId(), message);
    }
}
