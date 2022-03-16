package kr.co.won.config.security;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.auth.handler.LoginFailedHandler;
import kr.co.won.auth.handler.LoginSuccessHandler;
import kr.co.won.auth.handler.SimpleLoginFailedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Need to session sync SecurityConfiguration
 */
@Order(value = 0)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthBasicService authBasicService;
    private final SessionRegistry sessionRegistry;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** setting security ADMIN access /admin/*/

        http
                .antMatcher("/admin/**")
                .authorizeRequests()
                .mvcMatchers("/admin/login")
                .permitAll()
                .antMatchers("/admin/**")
                .hasAnyRole("ADMIN", "MANAGER")
                /** csrf and cors set */
                .and().csrf().and().cors()
                .and().headers().frameOptions().sameOrigin();
                /*.and().sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/duplicated-login")
                .sessionRegistry(sessionRegistry);*/
        http
                .antMatcher("/admin/**")
                .userDetailsService(authBasicService);

        /** form Login */
        http
                .antMatcher("/admin/**")
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/admin")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailedHandler)
                .defaultSuccessUrl("/admin")
                .failureHandler(new SimpleLoginFailedHandler("/admin/login?error=true"));
        /** logout */
        http
                .antMatcher("/admin/**")
                .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        /** static path ignore security */
        web.ignoring().mvcMatchers(
                PathRequest.toStaticResources().atCommonLocations().toString(),
                "/admin-resource/**", "/js/**", "/css/**", "/images/**", "/uploads"
        );
    }
}
