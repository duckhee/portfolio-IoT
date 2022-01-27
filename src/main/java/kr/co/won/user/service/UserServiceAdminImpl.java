package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service(value = "adminUserService")
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserService {

    private final UserPersistence userPersistence;

    @Override
    public UserDomain createUser(UserDomain newUser, UserDomain authUser, UserRoleType... roles) {
        /** login user check */
        UserDomain findUser = userPersistence.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        if (!findUser.roleCheck(UserRoleType.ADMIN) || !findUser.roleCheck(UserRoleType.MANAGER)) {
            throw new IllegalArgumentException("access denied.");
        }
        /** user roles set */
        Set<UserRoleDomain> userRoles = new HashSet<>();
        /** make user default role */
        UserRoleDomain defaultRole = UserRoleDomain.builder()
                .role(UserRoleType.USER)
                .build();
        userRoles.add(defaultRole);
        /** user role domain make */
        Arrays.stream(roles).forEach(roleType -> {
            if (!roleType.equals(UserRoleType.USER)) {
                UserRoleDomain makeRoles = UserRoleDomain.builder()
                        .role(roleType)
                        .build();
                userRoles.add(makeRoles);
            }
        });
        /** new user set roles */
        newUser.addRole(userRoles);
        UserDomain savedUser = userPersistence.save(newUser);

        return savedUser;
    }

    @Override
    public UserDomain findUser(Long userIdx, UserDomain authUser) {
        return UserService.super.findUser(userIdx, authUser);
    }

    private UserDomain LoginUserRoleCheck(UserDomain authUser, UserRoleType... roles) {
        UserDomain findUser = userPersistence.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        boolean flag = false;
        for (int i = 0; i < roles.length; i++) {
            flag = flag || findUser.roleCheck(roles[i]);
        }
        return findUser;
    }

}
