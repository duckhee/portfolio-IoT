package kr.co.won.config.security.jwt;

import kr.co.won.auth.AuthBasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;


// This is Testing Configruation
@Order(value = 4)
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final AuthBasicService authBasicService;

    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/jwt/**")
                .addFilterBefore(new JwtAuthFilter(authBasicService, jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .csrf()
                .disable()
                .authorizeRequests().antMatchers("/jwt/member", "/api/jwt-login")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
