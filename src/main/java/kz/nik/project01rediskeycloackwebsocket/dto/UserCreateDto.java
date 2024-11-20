package kz.nik.project01rediskeycloackwebsocket.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto implements Serializable {
    private String email;
    private String username;
    private String password;
}
