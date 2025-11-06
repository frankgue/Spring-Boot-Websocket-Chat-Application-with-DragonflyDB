package com.gkfcsolution.websocketchatdragonflybdspringboot.listener;

import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.ChatMessage;
import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Created on 2025 at 18:10
 * File: null.java
 * Project: websocket-chat-dragonflybd-springboot
 *
 * @author Frank GUEKENG
 * @date 06/11/2025
 * @time 18:10
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketEventListener {
    private final RedisTemplate<String, Object> redisTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }
    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null){
            ChatMessage chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .userName(username)
                    .message(username + " left the chat")
                    .build();
            log.info("User disconnected: {}", username);
            redisTemplate.convertAndSend("chat", chatMessage);
        }
    }
}
