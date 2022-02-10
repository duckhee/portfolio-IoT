package kr.co.won.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class SimpleLoginFailedHandler extends SimpleUrlAuthenticationFailureHandler {

    public SimpleLoginFailedHandler() {
        super("/login");
    }

    public SimpleLoginFailedHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // user not have
        if (exception.getCause() instanceof UsernameNotFoundException) {
            log.info("get user name not found ::: {}", exception.toString());
        }
        if (exception instanceof BadCredentialsException) {
            log.info("user not found ::: {}", exception.toString());
        }
        // user deleted
        if (exception.getCause() instanceof AccountExpiredException) {
            log.info("user deleted ::: {}", exception);
        }

        // user disabled
        if (exception.getCause() instanceof DisabledException) {
            log.info("get user disabled ::: {}", exception.toString());
        }
        // user not active
        if (exception.getCause() instanceof LockedException) {
            log.info("user is locked ::: {}", exception);
        }

        if (exception.getCause() instanceof BadCredentialsException) {
            log.info("get user login failed ::: {}", exception.toString());
        }

        if (exception.getCause() instanceof InternalAuthenticationServiceException) {
            log.info("not have user ::: {}", exception.toString());
        }

        if (exception instanceof AuthenticationException) {
            log.info("root failed ::: {}", exception.toString());
            log.info("exception ::: {}, exception code ::: {}", exception.getCause(), exception.hashCode());
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
