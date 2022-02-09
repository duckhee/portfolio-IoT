package kr.co.won.config.security;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** http security pattern add */
        http
                .antMatcher("/**")
                .authorizeRequests()
                /** api docs permitAll */
                .mvcMatchers("/docs/index.html", "/resources/**", "/resources/h2-console","/blogs/resources/**")
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

        /** remember-me add */


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /** static path ignore security */
        web.ignoring().mvcMatchers(
                PathRequest.toStaticResources().atCommonLocations().toString(),
                "/resources/h2-console",
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
