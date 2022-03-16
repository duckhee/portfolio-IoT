package kr.co.won.config.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import kr.co.won.auth.AuthBasicService;
import kr.co.won.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is using api path only
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthBasicService authBasicService;

    private final String LOGIN_PATH = "/api/auth-login";

    // token util
    private final JwtTokenUtil jwtTokenUtil;
    // api login path
    private static final List<String> EXCUTED_URL = Collections.unmodifiableList(Arrays.asList("/api/auth-login"));

    // jwt filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get header
        final String requestHeader = request.getHeader("Authorization");
        String userName = null;
        String jwtToken = null;
        if(requestHeader != null && requestHeader.startsWith("Bearer")){
            // get token name
            jwtToken = requestHeader.substring(7);
            try{
                userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
            }catch (IllegalArgumentException exception){
                log.info("unable to get JWT Token");
            }catch (ExpiredJwtException exception){
                log.info("expired time over token.");
            }
            // token get user name
        }else{
            log.warn("JWT Token does not have.");
        }
        // user token verified
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // login user check
            UserDetails user = authBasicService.loadUserByUsername(userName);

            if(jwtTokenUtil.validateToken(jwtToken, user)){
                // make jwt token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                // security context add principal
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // next filter do
        filterChain.doFilter(request, response);
    }

    // ignore path
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCUTED_URL.stream().anyMatch(execute->execute.equalsIgnoreCase(request.getServletPath()));
    }
}
