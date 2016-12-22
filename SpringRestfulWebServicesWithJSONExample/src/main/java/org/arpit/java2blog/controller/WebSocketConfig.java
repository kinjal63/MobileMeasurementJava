package org.arpit.java2blog.controller;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/gs-guide-websocket").addInterceptors(new HandshakeInterceptor() {
//			
//			@Override
//			public boolean beforeHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2,
//					Map<String, Object> arg3) throws Exception {
//				arg1.getHeaders().add("Access-Control-Allow-Origin", "*");
//				return true;
//			}
//			
//			@Override
//			public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
//				arg1.getHeaders().add("Access-Control-Allow-Origin", "*");
//				return;
//			}
//		}).withSockJS();
//    }

}