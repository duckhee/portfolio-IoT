package kr.co.won.mqtt.publish;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttPublish {


    /**
     * mqtt published event
     */
    public default void sendMsg(String topic, MqttMessage mqttMessage) {
        return;
    }
}
