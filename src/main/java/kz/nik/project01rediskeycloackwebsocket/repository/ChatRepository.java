package kz.nik.project01rediskeycloackwebsocket.repository;


import kz.nik.project01rediskeycloackwebsocket.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUsersContaining(String username);
}
