package kr.co.won.properties;

import lombok.Data;
import org.apache.ibatis.annotations.Property;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "mqtt")
public class MqttProperties {

    private String host = "127.0.0.1";

    private Integer port = 1883;

    private String userName = "localhost";

    private String userPassword = "";

    private String baseTopic = "#";
}
