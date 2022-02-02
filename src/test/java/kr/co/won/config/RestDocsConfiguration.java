package kr.co.won.config;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * Json 형태를 보기 쉽게 하기 위해서 설정을 위한 파일
 *
 * @author duckheewon
 */
@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        // restdocs snippets set pretty format
        return configurer -> configurer
                .uris()
                .withHost("example.co.kr")
                .withPort(80)
                .and()
                .snippets()
                .withEncoding(StandardCharsets.UTF_8.name())
                .and().operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }

}
