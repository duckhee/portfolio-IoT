package kr.co.won.config.security;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.config.security.jwt.JwtAuthEntryPoint;
import kr.co.won.config.security.jwt.JwtAuthFilter;
import kr.co.won.config.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(value = 2)
@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthBasicService authBasicService;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // security setting
        http.antMatcher("/api/**")
                .addFilterBefore(new JwtAuthFilter(authBasicService, jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users", "/api/auth-login") // user login path
                .permitAll()
                .mvcMatchers("/api/h2-console")
                .permitAll()
                // TODO Delete
//                .antMatchers("/api/users/**", "/api/blogs/**")
//                .permitAll()
                .anyRequest()
                .hasAnyRole("USER", "ADMIN", "MANAGER")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint);

        http.antMatcher("/api/**").headers()
                .frameOptions().sameOrigin().disable(); //x-frame-options 동일 출처일경우만
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
