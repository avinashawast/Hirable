package com.hirable.repository;

import com.hirable.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.sentAt ASC")
    List<Message> findByChatId(@Param("chatId") Long chatId);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.sentAt DESC LIMIT 50")
    List<Message> findRecentMessagesByChatId(@Param("chatId") Long chatId);
}
