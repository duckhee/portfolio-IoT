package kr.co.won.user.service;

import kr.co.won.auth.AuthUser;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.mail.EmailService;
import kr.co.won.properties.AppProperties;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {TestAppConfiguration.class, UserDomainBuilderFactory.class})
class UserServiceAdminImplTest {

    @Autowired
    private TestAppConfiguration testAppConfiguration = new TestAppConfiguration();

    @Autowired
    private UserDomainBuilderFactory userFactory = new UserDomainBuilderFactory();

    @Mock
    private AppProperties appProperties;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private EmailService emailService;

    @Spy
    @Resource(name = "")
    @MockBean
    private ModelMapper modelMapper = testAppConfiguration.modelMapper();

    @Spy
    @MockBean
    private PasswordEncoder passwordEncoder = testAppConfiguration.passwordEncoder();

    @InjectMocks
    @MockBean
    private UserServiceAdminImpl userService;


    @DisplayName(value = "00. Mock Admin user service get class Test")
    @Test
    void userServiceGetClassTests() {
        userService.getClass();
    }

    @DisplayName(value = "01. admin user service create Member Test")
    @Test
    void userServiceCreateUserTests_withSuccess() {
        UserDomain testAdmin = userFactory.makeDomainUser(1L, "admin", "admin@co.kr", "1234", UserRoleType.ADMIN);
        UserDomain testSaveUser = userFactory.makeDomainUser("tester", "testing@co.kr", "1234");
        log.info("get test admin user Role :: {}", testAdmin.getRoles().stream().map(UserRoleDomain::getRole).collect(Collectors.toList()));
        when(userPersistence.findByEmail(testAdmin.getEmail())).thenReturn(Optional.of(testAdmin));
        when(userPersistence.save(any())).thenReturn(userFactory.mockUser(testSaveUser, 2L, UserRoleType.USER));
        UserDomain saveUser = userService.createUser(testSaveUser, testAdmin, UserRoleType.USER);
        assertThat(saveUser.getEmail()).isEqualTo(testSaveUser.getEmail());
        assertThat(saveUser.hasRole(UserRoleType.USER)).isTrue();
        assertThat(saveUser.hasRole(UserRoleType.ADMIN)).isFalse();
        assertThat(saveUser.hasRole(UserRoleType.MANAGER)).isFalse();
    }
}