package com.hirable.repository;

import com.hirable.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE (c.recruiter.user.id = :userId OR c.jobSeeker.user.id = :userId) ORDER BY c.createdAt DESC")
    List<Chat> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Chat c WHERE c.recruiter.id = :recruiterId AND c.jobSeeker.id = :jobSeekerId")
    Optional<Chat> findByRecruiterAndJobSeeker(@Param("recruiterId") Long recruiterId, @Param("jobSeekerId") Long jobSeekerId);

    @Query("SELECT c FROM Chat c ORDER BY c.createdAt DESC")
    List<Chat> findAllChats();
}
