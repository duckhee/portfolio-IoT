package kr.co.won;

import kr.co.won.config.TestAppConfiguration;
import kr.co.won.user.domain.UserDomain;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(value = {TestAppConfiguration.class})
public class AppTest {

    private TestAppConfiguration testConfiguration = new TestAppConfiguration();

    private ModelMapper skipModelMapper = testConfiguration.modelMapper();
    private ModelMapper notSkipModelMapper = testConfiguration.notSkipModelMapper();

    @DisplayName(value = "01. model mapper skip mode tests")
    @Test
    void skipModelMapperTests() {
        UserDomain user = UserDomain.builder()
                .idx(1L)
                .name("testing")
                .email("testing@co.kr")
                .password("1234")
                .build();
        UserDomain mappedUser = notSkipModelMapper.map(user, UserDomain.class);
        Assertions.assertThat(user).isEqualTo(mappedUser);
        UserDomain updateUser = UserDomain.builder()
                .name("test")
                .build();
        notSkipModelMapper.map(updateUser, user);
        log.info("user ::: {}", user);
        log.info("update user ::: {}", updateUser);
    }

    @DisplayName(value = "01. model mapper not skip mode tests")
    @Test
    void notSkipModelMapperTests() {
        UserDomain user = UserDomain.builder()
                .idx(1L)
                .name("testing")
                .email("testing@co.kr")
                .password("1234")
                .build();
        UserDomain mappedUser = notSkipModelMapper.map(user, UserDomain.class);
        Assertions.assertThat(user).isEqualTo(mappedUser);
        UserDomain updateUser = UserDomain.builder()
                .name("test")
                .build();
        notSkipModelMapper.map(updateUser, user);
        log.info("user ::: {}", user);
        log.info("update user ::: {}", updateUser);
    }


}
