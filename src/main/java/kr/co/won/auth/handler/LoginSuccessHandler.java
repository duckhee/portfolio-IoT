package kr.co.won.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    private final UserService userService;

    /**
     * Login Done save Login Time logging
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser details = (LoginUser) auth.getPrincipal();
        userService.loggingLoginTime(details.getUser());
        super.onAuthenticationSuccess(request, response, authentication);*/
    }
}

