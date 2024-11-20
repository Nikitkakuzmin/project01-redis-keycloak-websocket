package kz.nik.project01rediskeycloackwebsocket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_name")
    private String chatName;

    @ElementCollection
    @CollectionTable(name = "chat_users", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "user_name")
    private List<String> users = new ArrayList<>();

}
