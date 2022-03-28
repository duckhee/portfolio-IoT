package kr.co.won.mqtt;


import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;


@MessagingGateway(defaultRequestChannel = "mqttOutBoundChannel")
public interface MqttGateWay {

    void sendMessage(@Header(MqttHeaders.TOPIC) String topic, @Payload String message);
}
