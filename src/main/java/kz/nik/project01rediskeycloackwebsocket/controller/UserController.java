package kz.nik.project01rediskeycloackwebsocket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kz.nik.project01rediskeycloackwebsocket.dto.UserCreateDto;
import kz.nik.project01rediskeycloackwebsocket.dto.UserDto;

import kz.nik.project01rediskeycloackwebsocket.dto.UserSignInDto;
import kz.nik.project01rediskeycloackwebsocket.model.Chat;
import kz.nik.project01rediskeycloackwebsocket.service.CacheService;
import kz.nik.project01rediskeycloackwebsocket.service.ChatMessageService;
import kz.nik.project01rediskeycloackwebsocket.service.UserService;

import kz.nik.project01rediskeycloackwebsocket.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CacheService cacheService; // Добавляем CacheService для кэширования
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

    @GetMapping()

    public List<UserDto> getUsers(HttpServletRequest httpRequest) {
        String cacheKey = "users";

        // Попытка получить список пользователей из кэша
        List<UserDto> cachedUsers = (List<UserDto>) cacheService.getCachedObject(cacheKey);
        if (cachedUsers != null) {
            return cachedUsers;
        }

        String token = httpRequest.getHeader("Authorization");
        List<UserDto> users = userService.getUsers(token);

        // Кэширование списка пользователей
        cacheService.cacheObject(cacheKey, users, 1, TimeUnit.MINUTES);

        return users;
    }

    @GetMapping(value = "{id}")
    public UserDto getUser(HttpServletRequest httpRequest, @PathVariable(name = "id") Long id) {
        String cacheKey = "user:" + id;

        // Попытка получить пользователя из кэша
        UserDto cachedUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }

        String token = httpRequest.getHeader("Authorization");
        UserDto user = userService.getUser(id, token);

        // Кэширование пользователя
        if (user != null) {
            cacheService.cacheObject(cacheKey, user, 1, TimeUnit.MINUTES);
        }

        return user;
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token, HttpSession session) {
        String cacheKey = "currentUser:" + token;

        // Попытка получить текущего пользователя из кэша
        UserDto cachedCurrentUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cachedCurrentUser != null) {
            return ResponseEntity.ok(cachedCurrentUser);
        }

        UserDto currentUser = userService.getCurrentUser(token);

        if (currentUser != null) {
            // Кэширование текущего пользователя
            cacheService.cacheObject(cacheKey, currentUser, 1, TimeUnit.MINUTES);

            // Сохранение текущего пользователя в сессии
            session.setAttribute("currentUser", currentUser);

            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add")
    public void addUser(@RequestBody UserCreateDto userCreateDto) {
        userService.addUser(userCreateDto);

        // Очистка кэша для списка пользователей
        cacheService.deleteCachedObject("users");
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userSignInDto) {
        String token = userService.signIn(userSignInDto);

        // Сохранение токена в сессии
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}
