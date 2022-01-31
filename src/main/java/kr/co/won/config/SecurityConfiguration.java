package kr.co.won.config;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.PathMatcher;

import javax.sql.DataSource;

/**
 * 우선 순위 설정
 */
@Order(value = 3)
//@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier(value = "ormDatasource")
    private final DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** http security pattern add */
        http
                .antMatcher("/**")
                .authorizeRequests()
                /** api docs permitAll */
                .mvcMatchers("/docs/index.html")
                .permitAll()
                .mvcMatchers("/**")
                .permitAll();

        /** form login add */

        /** remember-me add */

        /** http resources */
        http
                .antMatcher("/resources/**")
                .authorizeRequests()
                .mvcMatchers("/resources/h2-console")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /** static path ignore security */
        web.ignoring().mvcMatchers(
                PathRequest.toStaticResources().atCommonLocations().toString()
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
