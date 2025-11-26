package com.hirable.controller;

import com.hirable.dto.ApplicationDTO;
import com.hirable.dto.JobSeekerProfileDTO;
import com.hirable.service.ApplicationService;
import com.hirable.service.FileStorageService;
import com.hirable.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/jobseekers")
@CrossOrigin(origins = "http://localhost:4200")
public class JobSeekerController {
    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<JobSeekerProfileDTO> getProfile(@PathVariable Long id) {
        JobSeekerProfileDTO profile = jobSeekerService.getProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<JobSeekerProfileDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody JobSeekerProfileDTO profileDTO) {
        JobSeekerProfileDTO updated = jobSeekerService.updateProfile(id, profileDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String filename = jobSeekerService.uploadResume(id, file);
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload resume: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/applications")
    public ResponseEntity<ApplicationDTO> applyToJob(
            @PathVariable Long id,
            @RequestParam Long jobId) {
        try {
            ApplicationDTO application = applicationService.createApplication(id, jobId);
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationDTO>> getApplications(@PathVariable Long id) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByJobSeeker(id);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}/resume/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        try {
            JobSeekerProfileDTO profile = jobSeekerService.getProfile(id);
            
            if (profile.getResumeFilePath() == null || profile.getResumeFilePath().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = fileStorageService.getFilePath(profile.getResumeFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Determine content type based on file extension
            String contentType = getContentType(profile.getResumeFilePath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"resume." + getFileExtension(profile.getResumeFilePath()) + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
