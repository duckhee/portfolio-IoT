package kr.co.won.auth.handler;

import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    private final UserService userService;

    /**
     * Login Done save Login Time logging
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        UserDomain loginUser = (UserDomain) authentication.getPrincipal();
        log.info("logging login user ::: {}", loginUser);
        // send success handler
        super.onAuthenticationSuccess(request, response, authentication);
        /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser details = (LoginUser) auth.getPrincipal();
        userService.loggingLoginTime(details.getUser());
        super.onAuthenticationSuccess(request, response, authentication);
        */
    }
}

