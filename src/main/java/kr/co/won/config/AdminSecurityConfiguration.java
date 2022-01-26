package kr.co.won.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(value = 1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** setting security ADMIN access /admin/*/
        http
                .authorizeRequests()
                .mvcMatchers("/admin/login")
                .permitAll()
                .mvcMatchers("/admin/**")
                .hasAnyRole("ROLE_ADMIN");

        /** csrf and cors set */
        http
                .csrf();
        http
                .cors();
    }
}
