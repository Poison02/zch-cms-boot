package cdu.zch.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
@ConditionalOnProperty(name = "system.config.websocket-enabled")
// 告诉Spring框架要开启WebSocket消息代理的功能，并配置相关的端点和消息代理
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 /ws 的端点，进行WebSocket连接
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }
}
