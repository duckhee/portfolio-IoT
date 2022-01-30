package kr.co.won.user.factory;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@TestComponent
@RequiredArgsConstructor
public class UserFactory {

    private final UserPersistence userPersistence;
    private final PasswordEncoder passwordEncoder;

    public List<UserDomain> bulkInsertTestUser(int makeUserNumber, String name, String password) {
        if (makeUserNumber <= 0) {
            makeUserNumber = 100;
        }
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        List<UserDomain> users = new ArrayList<>();
        for (int i = 0; i < makeUserNumber; i++) {
            // make user domain
            UserDomain testUser = UserDomain.builder()
                    .name(name + i)
                    .email(name + i + "@co.kr")
                    .password(passwordEncoder.encode(password))
                    .address(testAddress)
                    .build();
            // user role make
            UserRoleDomain defaultRole = UserRoleDomain.builder()
                    .role(UserRoleType.USER)
                    .build();
            // user default role set
            testUser.addRole(defaultRole);
            users.add(testUser);
        }
        List<UserDomain> savedUserAll = userPersistence.saveAll(users);
        return savedUserAll;
    }
}