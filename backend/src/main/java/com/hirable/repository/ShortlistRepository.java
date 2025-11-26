package com.hirable.repository;

import com.hirable.entity.Shortlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShortlistRepository extends JpaRepository<Shortlist, Long> {
    List<Shortlist> findByRecruiterId(Long recruiterId);
    Optional<Shortlist> findByRecruiterIdAndJobSeekerId(Long recruiterId, Long jobSeekerId);
    void deleteByRecruiterIdAndJobSeekerId(Long recruiterId, Long jobSeekerId);
}
