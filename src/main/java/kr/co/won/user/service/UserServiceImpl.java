package kr.co.won.user.service;

import kr.co.won.mail.EmailService;
import kr.co.won.mail.form.VerifiedMessage;
import kr.co.won.properties.AppProperties;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service(value = "userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * user persistence
     */
    private final UserPersistence userPersistence;

    /** Email Service */
    private final EmailService emailService;

    private final AppProperties appProperties;


    /**
     * registe user
     */
    @Transactional
    @Override
    public UserDomain createUser(UserDomain newUser) {
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

    // send user confirm email
    private void sendConfirmEmail(UserDomain savedUser) {
        // generate email token
        savedUser.makeEmailToken();
        VerifiedMessage emailMsg = new VerifiedMessage();
        emailMsg.setHost(appProperties.getHost());
        emailMsg.setSubject("welcome portfolio site.");
        emailMsg.setUserEmail(savedUser.getEmail());
        emailMsg.setMessage(savedUser.getName()+" welcome my service.");
        emailMsg.setConfirmUri(appProperties.getHost()+"/users/confirm-email?email="+savedUser.getEmail()+"&token="+savedUser.getEmailCheckToken());
        emailService.sendConfirmEmail(emailMsg);
    }
}
