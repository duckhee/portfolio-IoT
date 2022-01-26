package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;

public interface UserService {

    /** registe User */
    public default UserDomain createUser(UserDomain newUser) {
        return null;
    }
}
