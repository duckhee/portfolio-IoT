package kr.co.won.gateway;

public interface MqttSubscription {

    public default void subscript() {
        return;
    }
}
