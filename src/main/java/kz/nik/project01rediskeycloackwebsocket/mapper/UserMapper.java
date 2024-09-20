package kz.nik.project01rediskeycloackwebsocket.mapper;

import kz.nik.project01rediskeycloackwebsocket.dto.UserDto;
import kz.nik.project01rediskeycloackwebsocket.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
