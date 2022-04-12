package kr.co.won.auth;

import kr.co.won.config.security.jwt.JwtTokenUtil;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@TestComponent
@RequiredArgsConstructor
public class JwtToken {

    private final UserPersistence userPersistence;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtils;
    private final AuthBasicService authService;

    // get user login token
    public String loginToken(String name, UserRoleType auth, String email, String password) {
        // test user domain make
        UserDomain testUser = UserDomain.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        // test user Role
        UserRoleDomain testUserRole = UserRoleDomain.builder()
                .role(auth)
                .build();
        testUser.addRole(testUserRole);
        // test user save db
        UserDomain savedTestUser = userPersistence.save(testUser);
        String token = jwtUtils.generateToken(savedTestUser.getEmail());
        return "Bearer " +token;
    }


    // get user login token
    public String loginToken(UserRoleType auth, String id, String password) {
        // test user domain make
        UserDomain testUser = UserDomain.builder()
                .name("testingUser")
                .email("testingUser@co.kr")
                .password(passwordEncoder.encode(password))
                .build();
        // test user Role
        UserRoleDomain testUserRole = UserRoleDomain.builder()
                .role(auth)
                .build();
        testUser.addRole(testUserRole);
        // test user save db
        UserDomain savedTestUser = userPersistence.save(testUser);
        String token = jwtUtils.generateToken(savedTestUser.getEmail());
//        UserDetails user = authService.jwtLogin(savedTestUser.getUserId(), password);
        return "Bearer " +token;
    }

    // get user token
    public String getSocketToken(UserRoleType auth, String id, String password) {
        // test user domain make
        UserDomain testUser = UserDomain.builder()
                .name("testingUser")
                .email("testingUser@co.kr")
                .password(passwordEncoder.encode(password))
                .build();
        // test user Role
        UserRoleDomain testUserRole = UserRoleDomain.builder()
                .role(auth)
                .build();
        testUser.addRole(testUserRole);
        // test user save db
        UserDomain savedTestUser = userPersistence.save(testUser);
        String token = jwtUtils.generateToken(savedTestUser.getEmail());
//        UserDetails user = authService.jwtLogin(savedTestUser.getUserId(), password);
        return token;
    }

    // get user login token
    public String loginToken(String id, String password) {

        String token = jwtUtils.generateToken(id);
        UserDetails user = authService.jwtLogin(id, password);
        return "Bearer " +token;
    }
}
