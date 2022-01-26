package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * user persistence
     */
    private final UserPersistence userPersistence;

    /**
     * registe user
     */
    @Override
    public UserDomain createUser(UserDomain newUser) {
        // user default role
        UserRoleDomain defaultRole = UserRoleDomain
                .builder()
                .build();
        newUser.addRole(defaultRole);
        UserDomain savedUser = userPersistence.save(newUser);
        return savedUser;
    }
}
