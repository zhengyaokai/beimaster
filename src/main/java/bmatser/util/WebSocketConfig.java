package bmatser.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(systemWebSocketHandler(),"/gdb_chat_websocket").setAllowedOrigins("*");
		registry.addHandler(systemWebSocketHandler(),"/gdb_chat_websocket/sockjs").setAllowedOrigins("*").withSockJS();
		registry.addHandler(systemWebSocketHandler(),"/websocket").setAllowedOrigins("*");
		registry.addHandler(systemWebSocketHandler(),"/websocket/sockjs").setAllowedOrigins("*").withSockJS();
    }
    public WebSocketHandler systemWebSocketHandler(){
        return new SystemWebSocketHandler();
    }
}
