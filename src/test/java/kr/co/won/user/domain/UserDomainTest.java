package kr.co.won.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * User Domain Unit Test
 */
class UserDomainTest {

    @DisplayName(value = "01. user domain builder test")
    @Test
    void userDomainBuilderTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = UserDomain.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
    }
}