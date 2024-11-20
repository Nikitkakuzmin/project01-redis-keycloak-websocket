package kz.nik.project01rediskeycloackwebsocket.service;


import kz.nik.project01rediskeycloackwebsocket.model.Chat;
import kz.nik.project01rediskeycloackwebsocket.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatMessageService {
    void saveMessage(Long chatId, String sender, String receiver, String content);
    List<ChatMessage> getMessages(Long chatId);

    Chat createChat(String chatName, List<String> users);
    List<Chat> getUserChats(String username);




}
