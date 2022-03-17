package kr.co.won.blog.service;

import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogMockFactory;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.blog.persistence.BlogResourcePersistence;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {TestAppConfiguration.class, UserDomainBuilderFactory.class, BlogMockFactory.class})
class BlogServiceImplTest {
    private TestAppConfiguration config = new TestAppConfiguration();
    private UserDomainBuilderFactory userFactory = new UserDomainBuilderFactory();

    private BlogMockFactory blogFactory = new BlogMockFactory();

    @Spy
    @Resource(name = "skipModelMapper")
    private ModelMapper modelMapper = config.modelMapper();

    @Spy
    @Resource(name = "notSkipModelMapper")
    private ModelMapper nullModelMapper = config.notSkipModelMapper();

    @MockBean
    private UserPersistence userPersistence;

    @MockBean
    private BlogPersistence blogPersistence;

    @MockBean
    private BlogReplyPersistence blogReplyPersistence;

    @MockBean
    private BlogResourcePersistence resourcePersistence;

    @InjectMocks
    @MockBean
    private BlogServiceImpl blogService;

    @DisplayName(value = "00. BlogService Class Tests")
    @Test
    void blogServiceMockTests() {
        log.info("get Blog Service :::: {}", blogService.getClass());
    }

    @DisplayName(value = "01. blog create service Tests")
    @Test
    void createBlogTests() {

    }
}