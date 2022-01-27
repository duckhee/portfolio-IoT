package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Service(value = "adminUserService")
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserService {

    private final UserPersistence userPersistence;

    @Override
    public UserDomain createUser(UserDomain newUser, UserDomain authUser, UserRoleType ... roles) {
        /** login user check */
        UserDomain findUser = userPersistence.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        if (!findUser.roleCheck(UserRoleType.ADMIN) || !findUser.roleCheck(UserRoleType.MANAGER)) {
            throw new IllegalArgumentException("access denied.");
        }

        UserRoleDomain defaultRole = UserRoleDomain.builder()
                .role(UserRoleType.USER)
                .build();

        return UserService.super.createUser(newUser, authUser, roles);
    }

    @Override
    public UserDomain findUser(Long userIdx, UserDomain authUser) {
        return UserService.super.findUser(userIdx, authUser);
    }
}
