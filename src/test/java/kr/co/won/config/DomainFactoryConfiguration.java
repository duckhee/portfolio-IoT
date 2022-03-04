package kr.co.won.config;

import kr.co.won.user.factory.UserDomainBuilderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DomainFactoryConfiguration {

    @Bean
    public UserDomainBuilderFactory userDomainBuilderFactory() {
        UserDomainBuilderFactory userDomainBuilderFactory = new UserDomainBuilderFactory();
        return userDomainBuilderFactory;
    }


}
