package kr.co.won.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttSubScriptImpl implements MqttSubScript {

    @Override
    public MessageHandler subscript() {
        log.info("testing");
        return message -> {
            log.info("get message ::: {}", message.toString());
        };
    }
}
