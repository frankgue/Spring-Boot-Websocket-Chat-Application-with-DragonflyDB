package com.gkfcsolution.websocketchatdragonflybdspringboot.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * Created on 2025 at 16:29
 * File: RedisMessageSubscriber.java.java
 * Project: websocket-chat-dragonflybd-springboot
 *
 * @author Frank GUEKENG
 * @date 06/11/2025
 * @time 16:29
 */
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Logic to broadcast the message to the clients
        String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
        try {
            ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
