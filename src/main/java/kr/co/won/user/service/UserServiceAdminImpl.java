package kr.co.won.user.service;

import kr.co.won.mail.EmailService;
import kr.co.won.mail.form.VerifiedMessage;
import kr.co.won.properties.AppProperties;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service(value = "adminUserService")
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserService {

    private final ModelMapper modelMapper;

    private final AppProperties appProperties;

    /**
     * email service
     */
    private final EmailService emailService;

    /**
     * password encoder
     */
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
        /** make random password create */
        String password = randomPassword();
        newUser.setPassword(password);
        /** new user password encoding */
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        UserDomain savedUser = userPersistence.save(newUser);
        /** send email user */
        sendConfirmEmailWithPassword(savedUser, password);
        return savedUser;
    }

    @Transactional
    @Override
    public UserDomain createUser(UserDomain newUser, UserDomain authUser, Set<UserRoleType> roles) {
        /** login user check */
        UserDomain findUser = userPersistence.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));

        if (!findUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            /** user Role check */
            log.info("admin user ::: {}", findUser);
            log.info("admin user role ::: {}", findUser.getRoles().toString());
            throw new IllegalArgumentException("access denied.");
        }
        /** user roles set */
        Set<UserRoleDomain> userRoles = new HashSet<>();
        /** make user default role */
        UserRoleDomain defaultRole = UserRoleDomain.builder()
                .role(UserRoleType.USER)
                .build();
        userRoles.add(defaultRole);
        Set<UserRoleDomain> collectRoles = roles.stream().map(userRoleType -> UserRoleDomain.builder().role(userRoleType).build()).collect(Collectors.toSet());
        userRoles.addAll(collectRoles);
        /** make random password create */
        String password = randomPassword();
        newUser.setPassword(password);
        /** new user password encoding */
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        UserDomain savedUser = userPersistence.save(newUser);
        /** send email user */
        sendConfirmEmailWithPassword(savedUser, password);
        return savedUser;
    }

    @Override
    public UserDomain findUser(Long userIdx, UserDomain authUser) {
        // Login User Role Check and Get User
        isAuth(authUser, UserRoleType.MANAGER, UserRoleType.ADMIN);
        UserDomain findUser = userPersistence.findWithRoleByIdx(userIdx).orElseThrow(() ->
                new IllegalArgumentException("not have user."));
        return findUser;
    }

    @Override
    public UserDomain findUserByEmail(String email, UserDomain authUser) {
        // Login User Role Check and Get User
        isAuth(authUser, UserRoleType.MANAGER, UserRoleType.ADMIN);
        UserDomain findUser = userPersistence.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("not have user."));
        return findUser;
    }

    @Override
    public Page pagingUser(PageDto page) {
        Pageable makePageable = page.makePageable(0, "idx");
        Page pagingResult = userPersistence.pagingUser(page.getType(), page.getKeyword(), makePageable);
        return pagingResult;
    }

    @Override
    public Page pagingUser(PageDto page, UserDomain authUser) {
        return UserService.super.pagingUser(page, authUser);
    }

    // login user role check
    private UserDomain isAuth(UserDomain authUser, UserRoleType... roles) {
        UserDomain findUser = userPersistence.findWithRoleByEmail(authUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("not login user."));
        /** user Role check */
        if (!findUser.hasRole(roles)) {
            throw new IllegalArgumentException("access denied.");
        }
        return findUser;
    }

    // make random password
    private String randomPassword() {
        String random = UUID.randomUUID().toString();
        String stripPassword = random.substring(1, 10);
        return stripPassword;
    }

    // send to user with initPassword
    private void sendConfirmEmailWithPassword(UserDomain newUser, String password) {
        String token = newUser.makeEmailToken();
        VerifiedMessage emailMsg = new VerifiedMessage();
        emailMsg.setHost(appProperties.getHost());
        emailMsg.setUserEmail(newUser.getEmail());
        emailMsg.setSubject("Welcome to my service");
        emailMsg.setConfirmUri(appProperties.getHost() + "/users/confirm-email?email=" + newUser.getEmail() + "&token=" + newUser.getEmailCheckToken());
        emailMsg.setPassword(password);
        emailService.sendConfirmEmail(emailMsg);

    }


}
