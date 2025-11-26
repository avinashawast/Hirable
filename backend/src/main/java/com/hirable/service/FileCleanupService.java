package com.hirable.service;

import com.hirable.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FileCleanupService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Periodically cleans up orphaned resume files
     * Runs daily at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOrphanedFiles() {
        // Get all valid resume filenames from database
        Set<String> validFilenames = new HashSet<>();
        
        jobSeekerRepository.findAll().forEach(jobSeeker -> {
            if (jobSeeker.getResumeFilePath() != null && !jobSeeker.getResumeFilePath().isEmpty()) {
                validFilenames.add(jobSeeker.getResumeFilePath());
            }
        });

        // Clean up files not in the valid set
        fileStorageService.cleanupOrphanedFiles(validFilenames);
    }
}
