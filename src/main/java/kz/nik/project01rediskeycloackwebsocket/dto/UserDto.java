package kz.nik.project01rediskeycloackwebsocket.dto;

import jakarta.persistence.Column;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String username;
    private String password;
}
