package kr.co.won.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
                .mvcMatchers("/api/h2-console")
                .permitAll()
                // TODO Delete
                .antMatchers("/api/users/**", "/api/blogs/**")
                .permitAll()
                .anyRequest()
                .hasAnyRole("USER", "ADMIN", "MANAGER");

        http.antMatcher("/api/**").headers()
                .frameOptions().sameOrigin(); //x-frame-options 동일 출처일경우만
        /** http csrf disable setting */
        http
                .csrf()
                .ignoringAntMatchers("/api/**");

        /** http cross domain setting */
        http
                .antMatcher("/api/**")
                .cors()
                .disable();
        /** http form login disable setting */
        /*http
                .antMatcher("/api/**")
                .formLogin()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers(PathRequest.toH2Console().toString());

    }
}
