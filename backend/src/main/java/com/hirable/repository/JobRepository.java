package com.hirable.repository;

import com.hirable.entity.Job;
import com.hirable.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);
    
    @Query(value = "SELECT * FROM jobs j WHERE j.status = CAST(:status AS job_status)", nativeQuery = true)
    List<Job> findByStatus(@Param("status") String status);
    
    @Query("SELECT j FROM Job j WHERE j.status = com.hirable.entity.JobStatus.APPROVED " +
           "AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR j.location = :location) " +
           "AND (:industry IS NULL OR j.industry = :industry) " +
           "AND (:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel) " +
           "ORDER BY j.postedAt DESC")
    Page<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("industry") String industry,
            @Param("experienceLevel") String experienceLevel,
            Pageable pageable);
}
