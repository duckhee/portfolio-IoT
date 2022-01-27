package kr.co.won.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(value = 2)
@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users")
                .permitAll()
                .anyRequest()
                .hasAnyRole("ROLE_USER");
        /** http csrf disable setting */
        http
                .csrf()
                .disable();

        /** http cross domain setting */
        http
                .cors()
                .disable();
    }
}
