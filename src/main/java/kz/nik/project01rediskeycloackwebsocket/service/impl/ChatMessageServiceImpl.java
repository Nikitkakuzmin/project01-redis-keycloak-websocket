package kz.nik.project01rediskeycloackwebsocket.service.impl;


import kz.nik.project01rediskeycloackwebsocket.model.Chat;
import kz.nik.project01rediskeycloackwebsocket.model.ChatMessage;
import kz.nik.project01rediskeycloackwebsocket.repository.ChatMessageRepository;
import kz.nik.project01rediskeycloackwebsocket.repository.ChatRepository;
import kz.nik.project01rediskeycloackwebsocket.service.CacheService;
import kz.nik.project01rediskeycloackwebsocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CacheService cacheService;


    public Chat createChat(String chatName, List<String> users) {
        Chat chat = new Chat();
        chat.setChatName(chatName);
        chat.setUsers(users);
        Chat savedChat = chatRepository.save(chat);

        // Сохранение нового чата в кэш
        String cacheKey = "chat:" + savedChat.getId();
        cacheService.cacheObject(cacheKey, savedChat, 1, TimeUnit.MINUTES);

        return savedChat;
    }

    public List<Chat> getUserChats(String username) {
        String cacheKey = "userChats:" + username;

        // Проверка наличия кэша для чатов пользователя
        List<Chat> cachedChats = (List<Chat>) cacheService.getCachedObject(cacheKey);
        if (cachedChats != null) {
            return cachedChats;
        }

        List<Chat> chats = chatRepository.findByUsersContaining(username);

        // Кэширование чатов пользователя
        cacheService.cacheObject(cacheKey, chats, 1, TimeUnit.MINUTES);
        return chats;
    }



    public void saveMessage(Long chatId, String sender, String receiver, String content) {
        log.info("Saving message in chat {} from {} to {}", chatId, sender, receiver);
        ChatMessage message = new ChatMessage();
        message.setChatId(chatId);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentTime(LocalDateTime.now());
        chatMessageRepository.save(message);

        // Очистка кэша сообщений чата после добавления нового сообщения
        cacheService.deleteCachedObject("chatMessages:" + chatId);
    }

    public List<ChatMessage> getMessages(Long chatId) {
        String cacheKey = "chatMessages:" + chatId;

        // Проверка кэша для сообщений
        List<ChatMessage> cachedMessages = (List<ChatMessage>) cacheService.getCachedObject(cacheKey);
        if (cachedMessages != null) {
            return cachedMessages;
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);

        // Кэширование сообщений
        cacheService.cacheObject(cacheKey, messages, 1, TimeUnit.MINUTES);
        return messages;
    }
}

