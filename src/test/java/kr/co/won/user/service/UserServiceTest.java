package kr.co.won.user.service;

import kr.co.won.address.Address;
import kr.co.won.mail.EmailService;
import kr.co.won.mail.EmailServiceConsoleImpl;
import kr.co.won.properties.AppProperties;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.config.Profiles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(value = {MockitoExtension.class})
class UserServiceTest {

    @Mock
    private UserPersistence userPersistence;


    @DisplayName(value = "01. user create service test - with SUCCESS")
    @Test
    void createUserWithSuccessTests() {
        EmailService emailService = new EmailServiceConsoleImpl();
        AppProperties appProperties = new AppProperties();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        appProperties.setHost("localhost");

        UserService userService = new UserServiceImpl(userPersistence, emailService, appProperties, passwordEncoder);
        // make test Address
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        // make test User
        String name = "testing";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain testUser = userBuilder(testAddress, name, email, password);
        when(userPersistence.save(testUser)).thenReturn(mockUser(testUser, 1L, UserRoleType.USER));
        UserDomain savedUser = userService.createUser(testUser);
        // save user test
        assertThat(savedUser.getIdx()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRoles().size()).isEqualTo(1);
        // user role check
        assertThat(savedUser.getRoles().stream().anyMatch(roleDomain -> roleDomain.getRole().equals(UserRoleType.USER))).isTrue();
    }


    private UserDomain userBuilder(Address testAddress, String name, String email, String password) {
        UserDomain testUser = UserDomain
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .address(testAddress)
                .build();
        return testUser;
    }

    private UserDomain mockUser(UserDomain user, long idx, UserRoleType... roles) {
        user.setIdx(idx);
        Set<UserRoleDomain> setRoles = new HashSet<>();
        for (int i = 0; i < roles.length; i++) {

            UserRoleDomain testRole = UserRoleDomain
                    .builder()
                    .role(roles[i])
                    .build();
            setRoles.add(testRole);
        }
        user.addRole(setRoles);
        return user;
    }

}