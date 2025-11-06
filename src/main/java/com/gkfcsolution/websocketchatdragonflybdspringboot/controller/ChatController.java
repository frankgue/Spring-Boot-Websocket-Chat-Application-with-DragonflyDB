package com.gkfcsolution.websocketchatdragonflybdspringboot.controller;

import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.ChatMessage;
import com.gkfcsolution.websocketchatdragonflybdspringboot.dto.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
@Slf4j
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    // Send Message to the client
    @MessageMapping("/chat.sendChatMessage")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // Add logic to send message to DragonFly DB QUEUE
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        return chatMessage;
    }

    // Add User to the application
    @MessageMapping("/chat.addUser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Get user name from the chatMessage object and add it to the Websocket Session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUserName());
        chatMessage.setMessageType(MessageType.JOIN);
        chatMessage.setMessage(chatMessage.getUserName() + " joined the chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("User joined: {}", chatMessage.getUserName());

        // Send the chat message back to the clients with message type as JOIN
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        return chatMessage;
    }

/*    @GetMapping("/")
    public String index(){
        return "index";
    }*/

}
