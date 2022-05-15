package kr.co.won.config.security;

import kr.co.won.auth.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.PathMatcher;

import javax.sql.DataSource;

/**
 * 우선 순위 설정
 */
@Order(value = 67)
//@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier(value = "ormDatasource")
    private final DataSource dataSource;
    private final SessionRegistry sessionRegistry;
    private final LoginSuccessHandler loginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** http security pattern add */
        http
                .antMatcher("/**")
                .authorizeRequests()
                /** api docs permitAll */
                .mvcMatchers("/docs/index.html", "/resources/**", "/resources/h2-console", "/blogs/resources/**")
                .permitAll()
                .mvcMatchers("/**")
//                .anyRequest()
                .permitAll()
                /** csrf and cors set */
                .and().csrf().and().cors()
                .and().headers().frameOptions().sameOrigin();
                /*.and().sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/duplicated-login")
                .sessionRegistry(sessionRegistry);*/

        /** form login add */
        http
                .antMatcher("/**")
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/")
                .defaultSuccessUrl("/")
                .successHandler(loginSuccessHandler)
                .permitAll();
        /** Social Login */
        /**
        http
                .antMatcher("/social-login/**")
                .authorizeRequests()
                .mvcMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login");
        */
        /** logout setting */
        http
                .antMatcher("/**")
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
        ;

        /** remember-me add */

        /** Web Socket path origin setting */
        http.antMatcher("/sockets/**")
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .disable();
        http.antMatcher("io/socket/**")
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .disable();
       /* http
                .antMatcher("/sock/**")
                .headers()
                .frameOptions()
                .sameOrigin()
                .xssProtection().disable()
                .and()
                .cors()
                .disable();*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /** static path ignore security */
        web.ignoring().mvcMatchers(
                PathRequest.toStaticResources().atCommonLocations().toString(),
                "/resources/h2-console", "/uploads",
                "/js/**", "/css/**", "/images/**"
        );
    }

    /**
     * Remember me token save repository
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
