package kr.co.won.config;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.auth.handler.LoginFailedHandler;
import kr.co.won.auth.handler.LoginSuccessHandler;
import kr.co.won.properties.AppProperties;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.core.session.SessionRegistry;

@TestConfiguration
@ExtendWith(value = {MockitoExtension.class})
public class TestWebMvcConfig {

    @MockBean
    private LoginSuccessHandler loginSuccessHandler;
    @MockBean
    private LoginFailedHandler loginFailedHandler;
    @MockBean
    private SessionRegistry sessionRegistry;
    @MockBean
    private AuthBasicService authBasicService;
    @MockBean
    private AppProperties appProperties;
    @MockBean
    private MessageSource messageSource;

}
