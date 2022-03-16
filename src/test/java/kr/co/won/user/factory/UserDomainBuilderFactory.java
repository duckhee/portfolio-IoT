package kr.co.won.user.factory;

import kr.co.won.common.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@TestComponent
@RequiredArgsConstructor
public class UserDomainBuilderFactory {


    public UserDomain makeDomainUser(String name, String email, String password) {
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        UserDomain testUser = userBuilder(testAddress, name, email, password);
        return testUser;
    }

    public UserDomain makeDomainUser(String name, String email, String password, UserRoleType role) {
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        UserDomain testUser = userBuilder(testAddress, name, email, password);
        UserRoleDomain testRole = testRoleBuilder(role);
        testUser.addRole(testRole);
        return testUser;
    }

    public UserDomain makeDomainUser(Long userIdx, String name, String email, String password, UserRoleType role) {
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        UserDomain testUser = userBuilder(testAddress, name, email, password);
        UserRoleDomain testRole = testRoleBuilder(role);
        testUser.addRole(testRole);
        testUser.setIdx(userIdx);
        return testUser;
    }

    public List<UserDomain> makeDomainBulkUser(String name, int number) {
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        List<UserDomain> result = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Long idx = Long.valueOf(i + 1);
            UserDomain makeUser = this.makeDomainUser(idx, name + i, name + i + "@co.kr", "1234", UserRoleType.USER);
            result.add(makeUser);
        }
        return result;
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
