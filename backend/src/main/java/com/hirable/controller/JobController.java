package com.hirable.controller;

import com.hirable.dto.JobDTO;
import com.hirable.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping("/recruiters/{id}/jobs")
    public ResponseEntity<List<JobDTO>> getRecruiterJobs(@PathVariable Long id) {
        List<JobDTO> jobs = jobService.getJobsByRecruiterId(id);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/recruiters/{id}/jobs")
    public ResponseEntity<JobDTO> createJob(
            @PathVariable Long id,
            @RequestBody JobDTO jobDTO) {
        JobDTO created = jobService.createJob(id, jobDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/recruiters/{id}/jobs/{jobId}")
    public ResponseEntity<JobDTO> updateJob(
            @PathVariable Long id,
            @PathVariable Long jobId,
            @RequestBody JobDTO jobDTO) {
        JobDTO updated = jobService.updateJob(jobId, jobDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/recruiters/{id}/jobs/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs/pending")
    public ResponseEntity<List<JobDTO>> getPendingJobs() {
        List<JobDTO> jobs = jobService.getPendingJobs();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/jobs/{id}/approve")
    public ResponseEntity<JobDTO> approveJob(@PathVariable Long id) {
        JobDTO approved = jobService.approveJob(id);
        return ResponseEntity.ok(approved);
    }

    @PutMapping("/jobs/{id}/reject")
    public ResponseEntity<JobDTO> rejectJob(
            @PathVariable Long id,
            @RequestParam String reason) {
        JobDTO rejected = jobService.rejectJob(id, reason);
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        JobDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<JobDTO>> searchJobs(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(defaultValue = "0") int page) {
        Page<JobDTO> jobs = jobService.searchJobs(keyword, location, industry, experienceLevel, page);
        return ResponseEntity.ok(jobs);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> removeJob(@PathVariable Long id) {
        jobService.removeJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs/all")
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        List<JobDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/status/{status}")
    public ResponseEntity<List<JobDTO>> getJobsByStatus(@PathVariable String status) {
        List<JobDTO> jobs = jobService.getJobsByStatus(status);
        return ResponseEntity.ok(jobs);
    }
}
