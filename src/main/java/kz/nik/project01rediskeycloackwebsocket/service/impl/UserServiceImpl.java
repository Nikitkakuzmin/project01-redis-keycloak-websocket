package kz.nik.project01rediskeycloackwebsocket.service.impl;

import kz.nik.project01rediskeycloackwebsocket.client.KeycloakClient;
import kz.nik.project01rediskeycloackwebsocket.dto.UserCreateDto;
import kz.nik.project01rediskeycloackwebsocket.dto.UserDto;

import kz.nik.project01rediskeycloackwebsocket.dto.UserSignInDto;
import kz.nik.project01rediskeycloackwebsocket.mapper.UserMapper;
import kz.nik.project01rediskeycloackwebsocket.model.User;
import kz.nik.project01rediskeycloackwebsocket.repository.UserRepository;
import kz.nik.project01rediskeycloackwebsocket.service.CacheService;
import kz.nik.project01rediskeycloackwebsocket.service.UserService;
import kz.nik.project01rediskeycloackwebsocket.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakClient keycloakClient;
    private final CacheService cacheService;

    @Override
    public List<UserDto> getUsers(String token) {
        String cacheKey = "users";

        // Проверка кэша для списка пользователей
        List<UserDto> cachedUsers = (List<UserDto>) cacheService.getCachedObject(cacheKey);
        if (cachedUsers != null) {
            return cachedUsers;
        }

        List<UserDto> users = userMapper.toDtoList(userRepository.findAll());

        // Кэширование пользователей
        cacheService.cacheObject(cacheKey, users, 1, TimeUnit.MINUTES);
        return users;
    }

    @Override
    public UserDto getUser(Long id, String token) {
        String cacheKey = "user:" + id;

        // Проверка кэша для конкретного пользователя
        UserDto cachedUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }

        UserDto user = userMapper.toDto(userRepository.findById(id).orElse(null));

        // Кэширование пользователя
        if (user != null) {
            cacheService.cacheObject(cacheKey, user, 1, TimeUnit.MINUTES);
        }

        return user;
    }

    public UserDto getCurrentUser(String token) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String cacheKey = "currentUser:" + username;

        // Проверка кэша для текущего пользователя
        UserDto cachedCurrentUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cachedCurrentUser != null) {
            return cachedCurrentUser;
        }

        UserDto currentUserDto = new UserDto();
        currentUserDto.setUsername(username);

        // Кэширование текущего пользователя
        cacheService.cacheObject(cacheKey, currentUserDto, 1, TimeUnit.MINUTES);
        return currentUserDto;
    }

    @Override
    public void addUser(UserCreateDto userCreateDto) {
        log.info("Creating user with username: {}", userCreateDto.getEmail());

        keycloakClient.addUser(userCreateDto);
        saveUserToDatabase(userCreateDto);

        // Очистка кэша для списка пользователей
        cacheService.deleteCachedObject("users");
    }

    private void saveUserToDatabase(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());

        userRepository.save(user);
    }

    public String signIn(UserSignInDto userSignInDto) {
        return keycloakClient.signIn(userSignInDto);
    }
}
