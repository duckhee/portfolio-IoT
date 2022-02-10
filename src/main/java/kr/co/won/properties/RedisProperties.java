package kr.co.won.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(value = "redis")
public class RedisProperties {

    private String host = "localhost";

    private int port = 6379;



}
