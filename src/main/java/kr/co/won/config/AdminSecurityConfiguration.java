package kr.co.won.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(value = 0)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
                .and().csrf().and().cors();
        /** csrf and cors set */

        /** form Login */
        http
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/admin")
                .defaultSuccessUrl("/admin")
                .and()
                .csrf();
        /** logout */
        http
                .logout()
                .logoutUrl("/logout")
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
                "/admin-resource/**", "/js/**","/css/**", "/images/**"
        );
    }
}
