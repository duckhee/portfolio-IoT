package kr.co.won.mqtt.template;

import kr.co.won.mqtt.MqttSubScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name        : MqttAbstractSubscript
 * Author           : Doukhee Won
 * Date             : 2022/05/15
 * Version          : v0.0.1
 * <p>
 * MQTT Subscript Abstract Class Template Pattern
 */

@Slf4j
public abstract class MqttAbstractSubscript implements MqttSubScript {


    @Override
    public MessageHandler subscript(String subscriptRootTopic) {
        return message -> {
            // get receive topic
            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
            // log information message
            log.info("message original ::: {}", message);
            log.info("get topic ::: {}", topic);
            // topic is null
            if (!topic.isBlank()) {
                log.info("topic is not null ::: {}", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));

                String payload = message.getPayload().toString();
                log.info("mqtt get topic ::: {}, get payload ::: {}", topic, payload);
                // if handling topic
                if (!topic.isBlank() && handlingTopic(subscriptRootTopic, topic)) {
                    // topic split command
                    String[] split = topic.split("/");
                    String command = split[split.length - 1];
                    log.info("get mqtt publish command :: {}", command);
                    // subscript data
                    if (command.equals("data")) {
                        // subscript logic
                        this.subscript(topic, payload);
                    }

                }
            }
        };
    }

    // TODO Handling Topic Super Topic or not get path slice and comparison
    // topic handling
    private boolean handlingTopic(String handleTopic, String topic) {
        log.info("get handling topic ::: {}, receive topic ::: {}", handleTopic, topic);
        // handling topic slice
        String handleRootTopic = handleTopic.substring(0, handleTopic.indexOf("/"));
        String[] splitHandleTopic = handleTopic.split("/");
        // receive topic slice
        String[] splitReceiveTopic = topic.split("/");
        String receiveRootTopic = topic.substring(0, topic.indexOf("/"));
        // compare Root Topic flag
        List<Boolean> compareFlag = new ArrayList<>();
        if (!handleRootTopic.equals("#") && !receiveRootTopic.equals("#") && !handleRootTopic.equals(receiveRootTopic)) {
            log.info("not match topic handle topic ::: {}, receive topic :: {}", handleRootTopic, receiveRootTopic);
            return false;
        }
        // compare topic base handle topic split
        for (int i = 0; i < splitHandleTopic.length; i++) {
            if (!splitHandleTopic[i].equals("#") && !splitReceiveTopic[i].equals("#") && !splitHandleTopic[i].equals(splitReceiveTopic[i])) {
                log.info("not match topic handle topic ::: {}, receive topic ::: {}", splitHandleTopic[i], splitReceiveTopic[i]);
                return false;
            }
        }
        return true;
    }

    /**
     * Subscript function concrete need
     */
    public abstract void subscript(String topic, String payload);
}
