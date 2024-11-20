package kz.nik.project01rediskeycloackwebsocket.controller;

import kz.nik.project01rediskeycloackwebsocket.client.KeycloakClient;
import kz.nik.project01rediskeycloackwebsocket.model.ChatCustomMessage;
import kz.nik.project01rediskeycloackwebsocket.model.ChatMessage;
import kz.nik.project01rediskeycloackwebsocket.service.ChatMessageService;
import kz.nik.project01rediskeycloackwebsocket.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final ChatMessageService chatMessageService;


    @MessageMapping("/chat/{chatId}/sendMessage")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessage sendMessage(@DestinationVariable Long chatId, ChatCustomMessage message) {
        chatMessageService.saveMessage(chatId, message.getSender(), message.getReceiver(), message.getContent());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatId);
        chatMessage.setSender(message.getSender());
        chatMessage.setReceiver(message.getReceiver());
        chatMessage.setContent(message.getContent());
        chatMessage.setSentTime(LocalDateTime.now());

        return chatMessage;
    }
}
