package kr.co.won.auth;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.CredentialExpiredException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthBasicService implements UserDetailsService {

    private final UserPersistence userPersistence;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDomain findUser = userPersistence.findByEmail(email).orElseThrow(() -> {
            log.info("Auth service not found user");
            throw new UsernameNotFoundException("해당되는 이메일의 사용자가 없습니다.");
        });
        // user delete
        if (findUser.isDeleteFlag()) {
            log.info("this user deleted.");
//            InternalAuthenticationServiceException
            throw new AccountExpiredException("not have user.");
        }
        // user email verified
        if (!findUser.isEmailVerified()) {
            log.info("first email verified");
            throw new DisabledException("이메일 인증을 먼저 해주시길 바랍니다.");
        }
        // active check
        if (!findUser.isActiveFlag()) {
            log.info("Auth service need to not active");
//            AuthenticationCredentialsNotFoundException
            throw new LockedException("계정을 활성화 해주세요.");
        }

        return new LoginUser(findUser);
    }


}
