package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;

public interface UserService {

    /**
     * registe User
     */
    public default UserDomain createUser(UserDomain newUser) {
        return null;
    }

    /**
     * create user
     */
    public default UserDomain createUser(UserDomain newUser, UserDomain authUser, UserRoleType... roles) {
        return null;
    }

    /**
     * Admin User Find User Service
     */
    public default UserDomain findUser(Long userIdx, UserDomain authUser) {
        return null;
    }
}
