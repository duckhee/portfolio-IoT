package kr.co.won.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailedHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof UsernameNotFoundException) {
            log.info("get user name not found ::: {}, code = {}", exception.toString(), exception.hashCode());
        }

        if (exception instanceof DisabledException) {
            log.info("get user disabled ::: {}", exception.toString());
        }
        if (exception instanceof AccountExpiredException) {
            log.info("user delete ::: {}", exception.toString());
        }
        if (exception instanceof BadCredentialsException) {
            log.info("get user login failed ::: {}", exception.toString());
        }

        if (exception instanceof InternalAuthenticationServiceException) {
            log.info("not have user ::: {}", exception.toString());
        }

        if (exception instanceof AuthenticationException) {
            log.info("root failed ::: {}", exception.toString());
        }
        response.sendRedirect("/admin/login");
    }
}
