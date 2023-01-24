package com.inpark.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        //prefix 확인해보려면 Tomcat Edit Configurations에서 Application Context부분 확인해보면 된다.
        registry.setApplicationDestinationPrefixes("/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //핸드 쉐이크할 url을 설정한다.
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
    }
}
