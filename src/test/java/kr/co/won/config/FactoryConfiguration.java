package kr.co.won.config;

import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@DataJpaTest
@TestConfiguration
@RequiredArgsConstructor
public class FactoryConfiguration {

    private final UserPersistence userPersistence;
    private final BlogPersistence blogPersistence;
    private final BlogReplyPersistence blogReplyPersistence;

    @Bean
    public UserDomainBuilderFactory userDomainBuilderFactory() {
        return new UserDomainBuilderFactory();
    }

    @Bean
    public UserFactory userFactory() {
        return new UserFactory(userPersistence);
    }

    @Bean
    public BlogFactory blogFactory() {
        return new BlogFactory(userPersistence, blogPersistence, blogReplyPersistence);
    }
}
