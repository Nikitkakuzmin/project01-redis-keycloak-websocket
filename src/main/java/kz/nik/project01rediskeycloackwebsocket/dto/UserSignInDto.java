package kz.nik.project01rediskeycloackwebsocket.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserSignInDto implements Serializable {
    private String username;
    private String password;
}
