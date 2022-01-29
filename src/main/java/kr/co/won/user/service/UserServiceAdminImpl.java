package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
@Service(value = "adminUserService")
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserService {

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * Login User is detach
     */

    private final UserPersistence userPersistence;


    @Transactional
    @Override
    public UserDomain createUser(UserDomain newUser, UserDomain authUser, UserRoleType... roles) {
        /** login user check */
        UserDomain findUser = userPersistence.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        if (!findUser.hasRole(UserRoleType.ADMIN) || !findUser.hasRole(UserRoleType.MANAGER)) {
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
        /** new user password encoding */
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        UserDomain savedUser = userPersistence.save(newUser);

        return savedUser;
    }


    @Override
    public UserDomain findUser(Long userIdx, UserDomain authUser) {
        // Login User Role Check and Get User
        UserDomain findAuthUser = hasAuth(authUser, UserRoleType.MANAGER, UserRoleType.ADMIN);
        UserDomain findUser = userPersistence.findWithRoleByIdx(userIdx).orElseThrow(()
                -> new IllegalArgumentException("not have user."));
        return findUser;
    }

    // login user role check
    private UserDomain hasAuth(UserDomain authUser, UserRoleType... roles) {
        UserDomain findUser = userPersistence.findWithRoleByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        if (!findUser.hasRole(roles)) {
            throw new IllegalArgumentException("access denied.");
        }
        return findUser;
    }

}
