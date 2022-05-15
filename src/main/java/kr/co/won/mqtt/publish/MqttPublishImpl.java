package kr.co.won.mqtt.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/**
 * File Name        : MqttPublishImpl
 * Author           : Doukhee Won
 * Date             : 2022/05/15
 * Version          : v0.0.1
 * <p>
 * mqtt publish topic
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttPublishImpl implements MqttPublish {

    // mqtt raw client
    private final IMqttClient mqttClient;

    /**
     * mqtt published topic
     */
    @Override
    public void sendMsg(String topic, MqttMessage mqttMessage) {
        log.info("mqtt published topic ::: {}, message ::: {}", topic, mqttMessage.toString());
        try {
            mqttClient.connect();
            mqttClient.publish(topic, mqttMessage);
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.warn("mqtt publish error !");
            e.printStackTrace();
        }
    }
}
