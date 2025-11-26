package com.hirable.repository;

import com.hirable.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobSeekerId(Long jobSeekerId);
    Optional<Application> findByJobSeekerIdAndJobId(Long jobSeekerId, Long jobId);
}
