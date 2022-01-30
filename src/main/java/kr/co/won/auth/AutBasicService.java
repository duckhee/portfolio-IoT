package kr.co.won.auth;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutBasicService implements UserDetailsService {

    private final UserPersistence userPersistence;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDomain findUser = userPersistence.findByEmail(email).orElseThrow(() -> {
            log.info("Auth service not found user");
            throw new UsernameNotFoundException("해당되는 이메일의 사용자가 없습니다.");
        });
        // user email verified
        if(!findUser.isEmailVerified()){
            log.info("first email verified");
            throw new AccessDeniedException("이메일 인증을 먼저 해주시길 바랍니다.");
        }
        // active check
        if(!findUser.isActiveFlag()){
            log.info("Auth service need to not active");
            throw new DisabledException("계정을 활성화 해주세요.");
        }

        return new LoginUser(findUser);
    }
}
