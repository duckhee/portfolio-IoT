package kr.co.won.user.domain;

import kr.co.won.user.domain.type.UserRoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * User Domain Unit Test
 */
class UserDomainTest {

    @DisplayName(value = "01. user domain builder Test")
    @Test
    void userDomainBuilderTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);

        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
    }

    @DisplayName(value = "02. user domain role add Test")
    @Test
    void userRoleAddTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);

        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());

        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);
        /** User Set Role  */
        builderUser.addRole(userRole);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        /** User Set Role  */
        builderUser.addRole(adminRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
        assertThat(builderUser.getRoles()).contains(userRole, adminRole);
    }

    @DisplayName(value = "02. user role multi add Test")
    @Test
    void userRoleAllAddTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);

        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());

        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);


        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        builderUser.addRole(userRole, adminRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
    }

    @DisplayName(value = "02. user role set add Test")
    @Test
    void userRoleAddSetTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);

        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());

        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);


        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);

        /** User Set make */
        HashSet<UserRoleDomain> userRoles = new HashSet<>();
        userRoles.add(userRole);
        userRoles.add(adminRole);

        builderUser.addRole(userRoles);

        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
    }

    @DisplayName(value = "03. user domain role remove Test")
    @Test
    void userRoleRemoveTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);
        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
        /** User Role Builder */
        UserRoleDomain userRole = userSetRole(builderUser, UserRoleType.USER);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** user Role add */
        UserRoleDomain adminRole = userSetRole(builderUser, UserRoleType.ADMIN);
        /** User Set Role  */
        builderUser.addRole(adminRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
        /** user Role remove */
        builderUser.removeRole(UserRoleType.ADMIN);
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        /** Default user Role Delete Failed */
        builderUser.removeRole(UserRoleType.USER);
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
    }

    @DisplayName(value = "03. user role multi remove Test")
    @Test
    void userRoleMultiDeleteTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);

        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());

        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);


        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        UserRoleDomain managerRole = makeRole(UserRoleType.MANAGER);
        builderUser.addRole(userRole, adminRole, managerRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(3);

        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(managerRole)).isTrue();
        assertThat(managerRole.getUser().equals(builderUser)).isTrue();
        builderUser.removeRole(UserRoleType.MANAGER, UserRoleType.ADMIN);
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(builderUser.getRoles().contains(adminRole)).isFalse();
        assertThat(adminRole.getUser()).isNull();
        assertThat(builderUser.getRoles().contains(managerRole)).isFalse();
        assertThat(managerRole.getUser()).isNull();
    }

    @DisplayName(value = "04. user role check Test")
    @Test
    void userRoleCheckTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);
        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);
        /** User Set Role  */
        builderUser.addRole(userRole);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        /** User Set Role  */
        builderUser.addRole(adminRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
        /** user Role Check Function */
        assertThat(builderUser.hasRole(UserRoleType.USER)).isTrue();
        assertThat(builderUser.hasRole(UserRoleType.MANAGER)).isFalse();
    }

    @DisplayName(value = "04. user role multi check Test")
    @Test
    void userRoleMultiCheckTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);
        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);
        /** User Set Role  */
        builderUser.addRole(userRole);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** user Role add */
        UserRoleDomain adminRole = makeRole(UserRoleType.ADMIN);
        /** User Set Role  */
        builderUser.addRole(adminRole);
        assertThat(builderUser.getRoles().size()).isEqualTo(2);
        assertThat(adminRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(adminRole)).isTrue();
        /** user Role Check Function */
        assertThat(builderUser.hasRole(UserRoleType.USER)).isTrue();
        assertThat(builderUser.hasRole(UserRoleType.ADMIN)).isTrue();
        assertThat(builderUser.hasRole(UserRoleType.MANAGER)).isFalse();
        assertThat(builderUser.hasRole(UserRoleType.USER, UserRoleType.ADMIN)).isTrue();
        /** user role strong check */
        assertThat(builderUser.hasAllMathRole(UserRoleType.ADMIN, UserRoleType.MANAGER)).isFalse();


    }

    @DisplayName(value = "05. user email check Token Create Test")
    @Test
    void userGenerateEmailTokenTests() {
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);
        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);
        /** User Set Role  */
        builderUser.addRole(userRole);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** User Email Token generate */
        String token = builderUser.makeEmailToken();
        assertThat(builderUser.getEmailCheckToken()).isNotNull();
        assertThat(builderUser.getEmailCheckToken().equals(token)).isTrue();
        assertThat(builderUser.isValidToken(token)).isTrue();
        /** user email token generate time */
        assertThat(builderUser.getEmailCheckTokenGeneratedTime()).isNotNull();
    }

    @DisplayName(value = "06. user join Test")
    @Test
    void userJoinFunctionTests(){
        String name = "tester";
        String email = "test@co.kr";
        String password = "1234";
        UserDomain builderUser = makeUserDomain(name, email, password);
        /** User Build Test */
        assertThat(builderUser.getName().equals(name)).isTrue();
        assertThat(builderUser.getEmail().equals(email));
        assertEquals(password, builderUser.getPassword());
        /** User Role Builder */
        UserRoleDomain userRole = makeRole(UserRoleType.USER);
        /** User Set Role  */
        builderUser.addRole(userRole);
        /** User Role Check */
        assertThat(builderUser.getRoles().size()).isEqualTo(1);
        assertThat(userRole.getUser().equals(builderUser)).isTrue();
        assertThat(builderUser.getRoles().contains(userRole)).isTrue();
        /** User Email Token generate */
        String token = builderUser.makeEmailToken();
        assertThat(builderUser.getEmailCheckToken()).isNotNull();
        assertThat(builderUser.getEmailCheckToken().equals(token)).isTrue();
        assertThat(builderUser.isValidToken(token)).isTrue();
        /** user email token generate time */
        assertThat(builderUser.getEmailCheckTokenGeneratedTime()).isNotNull();
        /** user Join */
        builderUser.join();
        assertThat(builderUser.getJoinTime()).isNotNull();
        assertThat(builderUser.isEmailVerified()).isTrue();
        assertThat(builderUser.isActiveFlag()).isTrue();
    }

    private UserRoleDomain userSetRole(UserDomain builderUser, UserRoleType user) {
        UserRoleDomain userRole = makeRole(user);
        /** User Set Role  */
        builderUser.addRole(userRole);
        return userRole;
    }

    private UserDomain makeUserDomain(String name, String email, String password) {
        return UserDomain.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    private UserRoleDomain makeRole(UserRoleType role) {
        return UserRoleDomain.builder()
                .role(role)
                .build();
    }
}