package kr.co.won.user;

import kr.co.won.common.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Profile(value = {"dev", "test"})
@Component
@Transactional
@RequiredArgsConstructor
public class TestUserData {

    private final UserPersistence userPersistence;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void InitUser() {
        log.info("Init User");
        Address address = new Address("zipCode", "roadAddress", "detailAddress");
        UserDomain testUser = UserDomain.builder()
                .email("testing@co.kr")
                .name("testing")
                .password(passwordEncoder.encode("1234"))
                .emailVerified(true)
                .job("DEVELOPER")
                .activeFlag(true)
                .deleteFlag(false)
                .address(address)
                .joinTime(LocalDateTime.now())
                .build();
        testUser.makeEmailToken();
        UserRoleDomain defaultRole = UserRoleDomain.builder()
                .role(UserRoleType.USER)
                .build();

        testUser.addRole(defaultRole);
        userPersistence.save(testUser);
    }
}
