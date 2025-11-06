package com.gkfcsolution.websocketchatdragonflybdspringboot.controller;

import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created on 2025 at 16:14
 * File: null.java
 * Project: websocket-chat-dragonflybd-springboot
 *
 * @author Frank GUEKENG
 * @date 06/11/2025
 * @time 16:14
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate redisTemplate;

    // Send Message to the client
    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage){
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // Add logic to send message to DragonFly DB QUEUE
        redisTemplate.convertAndSend("chat", chatMessage);
        return chatMessage;
    }

    // Add User to the application
}
