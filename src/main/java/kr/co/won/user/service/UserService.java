package kr.co.won.user.service;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;

import java.util.Set;

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
     * create member
     */
    public default UserDomain createUser(UserDomain newUser, UserDomain authUser, Set<UserRoleType> roles) {
        return null;
    }

    /**
     * Create User confirm Email
     */
    public default UserDomain emailConfirm(String email, String token) {
        return null;
    }

    /**
     * forget password method
     * Input UserEmail, UserName
     */
    public default void forgetPassword(UserDomain forgetUser) {
        return;
    }

    /**
     * Spring Security save Login User extends Users
     * User profile
     */
    public default UserDomain userProfile(UserDomain loginUser) {
        return null;
    }

    /**
     * Admin User Find User Service
     */
    public default UserDomain findUser(Long userIdx, UserDomain authUser) {
        return null;
    }

    /**
     * Admin User Find by user email
     */
    public default UserDomain findUserByEmail(String email, UserDomain authUser) {
        return null;
    }

    /**
     * update User
     */
    public default UserDomain updateUser(UserDomain updateUser, UserDomain loginUser) {
        return null;
    }

    public default UserDomain updateUser(Long userIdx, UserDomain updateUser) {
        return null;
    }

    public default UserDomain updateUser(Long userIdx, UserDomain updateUser, UserDomain loginUser) {
        return null;
    }

    /**
     * user list - with paging
     */
    public default Page pagingUser(PageDto page) {
        return null;
    }

    public default Page pagingUser(PageDto page, UserDomain authUser) {
        return null;
    }

}
