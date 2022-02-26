package kr.co.won.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.SuppressLoggerChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHandler;


public interface MqttSubScript {

    /**
     * log factory
     */
    public default MessageHandler subscript() {
        Logger log = LoggerFactory.getLogger("log");
        MessageHandler messageHandler = message -> log.info("get default subscript interface message ::: {}", message);
        return messageHandler;
    }
}
