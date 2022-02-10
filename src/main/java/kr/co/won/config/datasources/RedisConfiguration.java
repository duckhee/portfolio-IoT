package kr.co.won.config.datasources;

import groovy.transform.ConditionalInterrupt;
import kr.co.won.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

//    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(redisProperties.getHost());
        redisConfiguration.setPort(redisProperties.getPort());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        return lettuceConnectionFactory;

    }
}
