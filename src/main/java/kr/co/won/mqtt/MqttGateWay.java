package kr.co.won.mqtt;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Payloads;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateWay {

    void sendMessage(@Header(MqttHeaders.TOPIC) String topic, @Payload String message);
}
