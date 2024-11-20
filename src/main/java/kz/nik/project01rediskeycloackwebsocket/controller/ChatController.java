package kz.nik.project01rediskeycloackwebsocket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kz.nik.project01rediskeycloackwebsocket.dto.ChatCustomMessageDto;
import kz.nik.project01rediskeycloackwebsocket.model.Chat;
import kz.nik.project01rediskeycloackwebsocket.model.ChatCreateRequest;
import kz.nik.project01rediskeycloackwebsocket.model.ChatCustomMessage;
import kz.nik.project01rediskeycloackwebsocket.model.ChatMessage;
import kz.nik.project01rediskeycloackwebsocket.service.CacheService;
import kz.nik.project01rediskeycloackwebsocket.service.ChatMessageService;
import kz.nik.project01rediskeycloackwebsocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;


    @GetMapping(value = "/session")
    public String getSession(HttpSession session) {
        return "session : " + session;
    }

    @GetMapping(value = "/setup/{name}")
    public String setup(@PathVariable(name = "name") String name, HttpSession session) {
        session.setAttribute("userName", name);
        return "saved";
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestBody ChatCreateRequest request) {
        Chat chat = chatMessageService.createChat(request.getChatName(), request.getUsers());

        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatId));
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Void> sendMessage(@PathVariable Long chatId, @RequestBody ChatCustomMessage message) {
        chatMessageService.saveMessage(chatId, message.getSender(), message.getReceiver(), message.getContent());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/chats")
    public ResponseEntity<List<Chat>> getUserChats(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        List<Chat> userChats = chatMessageService.getUserChats(token); // Получение чатов пользователя
        return ResponseEntity.ok(userChats);
    }

}

