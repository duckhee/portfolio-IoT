package kr.co.won.user.persistence;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserPersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserPersistence userPersistence;

    @DisplayName(value = "01. user create Test")
    @Test
    void userPersistenceCrateTest() {
        String name = "tester";
        String email = "tester@co.kr";
        String password = "1234";
        UserDomain testUser = makeUserDomain(name, email, password);
        UserRoleDomain testRole = makeRole(UserRoleType.USER);
        testUser.addRole(testRole);
        UserDomain savedUser = userPersistence.save(testUser);
        assertThat(savedUser.getIdx()).isNotNull();
        assertThat(savedUser.equals(testUser)).isTrue();
        assertThat(testRole.getIdx()).isNotNull();
    }

    @DisplayName(value = "02. user find Test")
    @Test
    void userPersistenceFindTest() {
        String name = "tester";
        String email = "tester@co.kr";
        String password = "1234";
        UserRoleDomain defaultRole = makeRole(UserRoleType.USER);
        UserDomain savedUser = savedUser(name, email, password, defaultRole);
        // persistence reset
        entityManager.clear();
        // find persistence test
        assertDoesNotThrow(() -> {
            userPersistence.findByIdx(savedUser.getIdx()).orElseThrow(() -> new IllegalArgumentException(""));
        });
    }

    @DisplayName(value = "02. user find test - with EntityGraph")
    @Test
    void userPersistenceFindWithRoleTest() {
        String name = "tester";
        String email = "tester@co.kr";
        String password = "1234";
        UserRoleDomain defaultRole = makeRole(UserRoleType.USER);
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        UserDomain savedUser = savedUser(name, email, password, defaultRole, adminRole);
        // persistence reset
        entityManager.clear();
        // find persistence test
        UserDomain findUser = assertDoesNotThrow(() ->
                userPersistence.findWithRoleByEmail(savedUser.getEmail()).orElseThrow(() -> new IllegalArgumentException(""))
        );
        // transaction close
        entityManager.close();
        assertThat(findUser.getRoles().size()).isEqualTo(2);
        assertThat(findUser.getRoles()).contains(defaultRole, adminRole);
    }

    @DisplayName(value = "03. user update test")
    @Test
    void userPersistenceUpdateTest() {

    }



    private UserRoleDomain userSetRole(UserDomain builderUser, UserRoleType user) {
        UserRoleDomain userRole = makeRole(user);
        /** User Set Role  */
        builderUser.addRole(userRole);
        return userRole;
    }

    private UserDomain makeUserDomain(String name, String email, String password) {
        Address address = new Address("zipCode", "roadAddress", "detailAddress");
        return UserDomain.builder()
                .name(name)
                .email(email)
                .password(password)
                .address(address)
                .build();
    }

    private UserRoleDomain makeRole(UserRoleType role) {
        return UserRoleDomain.builder()
                .role(role)
                .build();
    }

    private UserDomain savedUser(String name, String email, String password, UserRoleDomain... roles) {
        UserDomain testUser = makeUserDomain(name, email, password);
        testUser.addRole(roles);
        UserDomain savedUser = userPersistence.save(testUser);
        return savedUser;
    }

}