package kr.co.won.auth;

import kr.co.won.common.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class TestMockUserFactory implements WithSecurityContextFactory<TestMockUser> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(TestMockUser annotation) {
        String name = annotation.name();
        String email = annotation.email();
        String password = annotation.password();
        Address testAddress = new Address("zipCode", "roadAddress", "detailAddress");
        UserRoleType testUserRole = annotation.authLevel();
        // test user builder
        UserDomain testUser = UserDomain
                .builder()
                .name(name)
                .email(email)
                .address(testAddress)
                .password(passwordEncoder.encode(password))
                .build();

        // test user role builder
        UserRoleDomain testRole = UserRoleDomain.builder()
                .role(testUserRole)
                .build();

        // test user add role
        testUser.addRole(testRole);


        // security context add test user - this is login done token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new LoginUser(testUser),
                testUser.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + testRole.getRole())));
        // get security context
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // set security context
        securityContext.setAuthentication(token);

        return securityContext;
    }

}
