package kr.co.won.user.factory;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import org.springframework.boot.test.context.TestComponent;

import java.util.HashSet;
import java.util.Set;

@TestComponent
public class UserDomainBuilderFactory {

    public UserDomain makeDomainUser(Long userIdx, String name, String email, String password, UserRoleType role) {
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        UserDomain testUser = userBuilder(testAddress, name, email, password);
        UserRoleDomain testRole = testRoleBuilder(role);
        testUser.addRole(testRole);
        testUser.setIdx(userIdx);
        return testUser;
    }

    // user domain
    public UserDomain userBuilder(Address testAddress, String name, String email, String password) {
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
    public UserRoleDomain testRoleBuilder(UserRoleType manager) {
        UserRoleDomain managerRole = UserRoleDomain.builder()
                .role(manager)
                .build();
        return managerRole;
    }

    // mock user setting
    public UserDomain mockUser(UserDomain user, long idx, UserRoleType... roles) {
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
