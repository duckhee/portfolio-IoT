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
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.config.Profiles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
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
        // confirm logging
        log.info("create user domain ::: {}", savedUser.toString());
        log.info("create user domain ::: {}", savedUser.getRoles().toString());
    }

    @DisplayName(value = "01. user create admin service test - with SUCCESS")
    @Test
    void createAdminUserWithSuccessTests() {
        EmailService emailService = new EmailServiceConsoleImpl();
        AppProperties appProperties = new AppProperties();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ModelMapper modelMapper = new ModelMapper();
        appProperties.setHost("localhost");
        // make test Address
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        // make test User
        String name = "testing";
        String email = "test@co.kr";
        String adminEmail = "admin@co.kr";
        String password = "1234";
        Set<UserRoleType> setRole = new HashSet<>();
        UserRoleType managerRole = UserRoleType.MANAGER;
        UserRoleType defaultRole = UserRoleType.USER;
        setRole.add(managerRole);
        /** user service */
        UserService userService = new UserServiceAdminImpl(modelMapper, appProperties, emailService, passwordEncoder, userPersistence);

        /** make admin user */
        UserDomain adminUser = userBuilder(testAddress, name, adminEmail, password);
        UserDomain testUser = userBuilder(testAddress, name, email, password);

        /** mockito make user */
        given(userPersistence.findByEmail(adminUser.getEmail()))
                .willReturn(Optional.of(mockUser(adminUser, 2L, UserRoleType.ADMIN, UserRoleType.USER)));
        given(userPersistence.save(testUser))
                .willReturn(mockUser(testUser, 1L, UserRoleType.USER, UserRoleType.MANAGER));

        // admin call member create
        UserDomain testAdminCreateMember = userService.createUser(testUser, adminUser, setRole);

        /** assert create member */
        assertThat(testAdminCreateMember.getName()).isNotNull();
        assertThat(testAdminCreateMember.getEmail()).isNotNull();
        assertThat(testAdminCreateMember.getIdx()).isNotNull();
        assertThat(testAdminCreateMember.getAddress()).isNotNull();
        // user role check
        assertThat(testAdminCreateMember.hasRole(defaultRole, managerRole)).isTrue();
        assertThat(testAdminCreateMember.getRoles().size()).isEqualTo(2);
        assertThat(testAdminCreateMember.getEmailCheckToken()).isNotNull();
        // confirm logging
        log.info("create admin user domain ::: {}", adminUser.toString());
        log.info("create admin user domain ::: {}", adminUser.getRoles().toString());
        // confirm logging
        log.info("create user domain ::: {}", testAdminCreateMember.toString());
        log.info("create user domain ::: {}", testAdminCreateMember.getRoles().toString());

    }

    @DisplayName(value = "01. user create admin service test admin user not have role - with failed")
    @Test
    void createAdminUserWithFailedAdminUserNotHaveRoleTests() {
        EmailService emailService = new EmailServiceConsoleImpl();
        AppProperties appProperties = new AppProperties();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ModelMapper modelMapper = new ModelMapper();
        appProperties.setHost("localhost");
        // make test Address
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        // make test User
        String name = "testing";
        String email = "test@co.kr";
        String adminEmail = "admin@co.kr";
        String password = "1234";
        Set<UserRoleType> setRole = new HashSet<>();
        UserRoleType managerRole = UserRoleType.MANAGER;
        UserRoleType defaultRole = UserRoleType.USER;
        setRole.add(managerRole);
        /** user service */
        UserService userService = new UserServiceAdminImpl(modelMapper, appProperties, emailService, passwordEncoder, userPersistence);

        /** make admin user */
        UserDomain adminUser = userBuilder(testAddress, name, adminEmail, password);
        UserDomain testUser = userBuilder(testAddress, name, email, password);

        /** mockito make user */
        given(userPersistence.findByEmail(adminUser.getEmail()))
                .willReturn(Optional.of(mockUser(adminUser, 2L, UserRoleType.USER)));

        /*
        given(userPersistence.save(testUser))
                .willReturn(mockUser(testUser, 3L, UserRoleType.USER, UserRoleType.MANAGER));
        */

        // admin call member create failed admin user not have
        assertThrows((IllegalArgumentException.class),
                () -> userService.createUser(testUser, adminUser, setRole));

    }

    // user domain
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

    // test user role
    private UserRoleDomain testRoleBuilder(UserRoleType manager) {
        UserRoleDomain managerRole = UserRoleDomain.builder()
                .role(manager)
                .build();
        return managerRole;
    }

    // mock user setting
    private UserDomain mockUser(UserDomain user, long idx, UserRoleType... roles) {
        user.setIdx(idx);
        Set<UserRoleDomain> setRoles = new HashSet<>();
        for (int i = 0; i < roles.length; i++) {

            UserRoleDomain testRole = testRoleBuilder(roles[i]);
            setRoles.add(testRole);
        }
        user.addRole(setRoles);
        return user;
    }

}