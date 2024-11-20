package kz.nik.project01rediskeycloackwebsocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatCustomMessage implements Serializable {
    private String sender;
    private String content;
    private String receiver;
}
