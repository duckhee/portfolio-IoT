package kr.co.won.user.service;

import kr.co.won.auth.LoginUser;
import kr.co.won.mail.EmailService;
import kr.co.won.mail.form.VerifiedMessage;
import kr.co.won.properties.AppProperties;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Transactional(readOnly = true)
@Service(value = "userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * user persistence
     */
    private final UserPersistence userPersistence;

    /**
     * Email Service
     */
    private final EmailService emailService;

    private final AppProperties appProperties;
    /**
     * password Encoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * registe user
     */
    @Transactional
    @Override
    public UserDomain createUser(UserDomain newUser) {
        // password encoding
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        // user default role
        UserRoleDomain defaultRole = UserRoleDomain
                .builder()
                .build();
        newUser.addRole(defaultRole);
        UserDomain savedUser = userPersistence.save(newUser);
        // send confirm email
        sendConfirmEmail(savedUser);
        return savedUser;
    }

    @Override
    public UserDomain emailConfirm(String email, String token) {
        // find user by email
        UserDomain findUser = userPersistence.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("not have user."));
        // user email token check
        // TODO Check return null or throw exception
        if (!findUser.isValidToken(token)) {
            new IllegalArgumentException("not valid token.");
        }
        // user join
        findUser.join();
        return findUser;
    }

    @Override
    public void forgetPassword(UserDomain forgetUser) {

        UserService.super.forgetPassword(forgetUser);
    }

    // send user confirm email
    private void sendConfirmEmail(UserDomain savedUser) {
        // generate email token
        savedUser.makeEmailToken();
        VerifiedMessage emailMsg = new VerifiedMessage();
        emailMsg.setHost(appProperties.getHost());
        emailMsg.setSubject("welcome portfolio site.");
        emailMsg.setUserEmail(savedUser.getEmail());
        emailMsg.setMessage(savedUser.getName() + " welcome my service.");
        emailMsg.setConfirmUri(appProperties.getHost() + "/users/confirm-email?email=" + savedUser.getEmail() + "&token=" + savedUser.getEmailCheckToken());
        emailService.sendConfirmEmail(emailMsg);
    }

    // user login set Security
    private void userLogin(UserDomain user) {
        // Basic User Login UsernamePasswordToken
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new LoginUser(user),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }
}
