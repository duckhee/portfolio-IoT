package kr.co.won.auth;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Transactional(readOnly = true)
public class LoginUser extends User {

    private UserDomain user;

    public LoginUser(UserDomain user) {
        // login user set
        super(user.getEmail(), user.getPassword(), makeGrantedAuthorities(user.getRoles()));
        this.user = user;
    }
    // make user role
    private static Collection<SimpleGrantedAuthority> makeGrantedAuthorities(Set<UserRoleDomain> roles) {
        // role
        List<SimpleGrantedAuthority> authRoles = new ArrayList<>();
        roles.forEach(roleDomain -> authRoles.add(new SimpleGrantedAuthority("ROLE_" + roleDomain.getRole())));
        return authRoles;
    }
}
