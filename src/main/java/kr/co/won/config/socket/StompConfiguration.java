package kr.co.won.config.socket;

import kr.co.won.config.socket.stomp.StompAuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP Auth Handler
     */
    private final StompAuthHandler stompAuthHandler;

    // client msg send controller
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //socket endpoint setting
        //최초 소켓 연결을 하는 경우, endpoint가 되는 url이다. 추후 javascript에서 SockJS 생성자를 통해 연결될 것이다.
        registry.addEndpoint("/sb/sockets")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS().setWebSocketEnabled(true);
    }

    // client subscript
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // prefix setting
        //enableSimpleBroker는 클라이언트로 메세지를 응답해줄 때 prefix를 정의한다.
        registry.enableSimpleBroker("/topic/chat", "/topic/iot");
        // controller end point pub prefix client send message request handle
        //setApplicationDestinationPrefixes는 클라이언트에서 메세지 송신 시 붙여줄 prefix를 정의한다.
        registry.setApplicationDestinationPrefixes("/");
    }

    /**
     * Login Filter Interceptor config
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // interceptor add
        registration.interceptors(stompAuthHandler);
    }
}
