package kz.nik.project01rediskeycloackwebsocket.service;

import kz.nik.project01rediskeycloackwebsocket.dto.UserCreateDto;
import kz.nik.project01rediskeycloackwebsocket.dto.UserDto;
import kz.nik.project01rediskeycloackwebsocket.dto.UserSignInDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void addUser(UserCreateDto userCreateDto);

    String signIn(UserSignInDto userSignInDto);

    UserDto getCurrentUser(String token);

    List<UserDto> getUsers(String token);

    UserDto getUser(Long id, String token);

}
