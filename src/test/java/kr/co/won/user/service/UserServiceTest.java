package kr.co.won.user.service;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        UserService userService = new UserServiceImpl(userPersistence);
        // make test Address
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        // make test User
        String name = "testing";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain testUser = UserDomain
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .address(testAddress)
                .build();
        when(userPersistence.save(testUser)).thenReturn(mockUser(testUser, UserRoleType.USER, 1L));
        UserDomain savedUser = userService.createUser(testUser);
        // save user test
        assertThat(savedUser.getIdx()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getPassword()).isEqualTo(password);
        assertThat(savedUser.getRoles().size()).isEqualTo(1);
        // user role check
        assertThat(savedUser.getRoles().stream().anyMatch(roleDomain -> roleDomain.getRole().equals(UserRoleType.USER))).isTrue();
    }

    private UserDomain mockUser(UserDomain user, UserRoleType role, long idx) {
        user.setIdx(idx);
        UserRoleDomain testRole = UserRoleDomain
                .builder()
                .role(role)
                .build();
        user.addRole(testRole);
        return user;
    }

}