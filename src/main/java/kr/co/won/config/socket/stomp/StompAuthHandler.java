package kr.co.won.config.socket.stomp;

import kr.co.won.auth.AuthBasicService;
import kr.co.won.config.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * STOMP Auth Handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthHandler implements ChannelInterceptor {

    /**
     * STOMP Message Mapper before check jwt token
     */

    /**
     * JWT Token util
     */
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Login Service
     */
    private final AuthBasicService authService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("get message channel ::: {}, get message ::: {}", channel, message);
        // get stomp socket header information
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        // get socket connected
        if (stompHeaderAccessor.getCommand() == StompCommand.CONNECT) {
            log.info("get connection socket ");
            // get token Authorization
            String jwtToken = stompHeaderAccessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            log.info("get jwt Token ::: {}", jwtToken);
            // token get user name
            String userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
            log.info("get token user name ::: {}", userName);
            // get token user id using login
            UserDetails userDetails = authService.loadUserByUsername(userName);
            log.info("get user detail ::: {}", userDetails.getUsername());
            // check token value
            if (!jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                log.info("get socket auth failed");
                throw new AccessDeniedException("login first");
            }
            log.info("login success user :::: {},  get msg ::: {}", userDetails.getUsername(), message);
        }
        log.info("get interceptor message ::: {}", message);
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
