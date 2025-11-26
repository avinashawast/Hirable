package com.hirable.repository;

import com.hirable.entity.ModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModerationLogRepository extends JpaRepository<ModerationLog, Long> {
    @Query("SELECT ml FROM ModerationLog ml WHERE ml.chat.id = :chatId ORDER BY ml.createdAt DESC")
    List<ModerationLog> findByChatId(@Param("chatId") Long chatId);

    @Query("SELECT ml FROM ModerationLog ml WHERE ml.targetUser.id = :userId ORDER BY ml.createdAt DESC")
    List<ModerationLog> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ml FROM ModerationLog ml ORDER BY ml.createdAt DESC")
    List<ModerationLog> findAllLogs();
}
