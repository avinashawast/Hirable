package com.hirable.service;

import com.hirable.exception.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${hirable.file.upload-dir:./uploads/resumes}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");

    /**
     * Saves a file with UUID-based naming to prevent collisions
     * @param file the file to save
     * @return the saved filename
     * @throws IOException if file operations fail
     */
    public String saveFile(MultipartFile file) throws IOException {
        validateFile(file);
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        // Generate unique filename with UUID
        String filename = UUID.randomUUID() + "." + getFileExtension(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.write(filePath, file.getBytes());

        return filename;
    }

    /**
     * Retrieves the full path to a stored file
     * @param filename the filename to retrieve
     * @return the full file path
     */
    public Path getFilePath(String filename) {
        // Sanitize filename to prevent path traversal attacks
        String sanitizedFilename = sanitizeFilename(filename);
        return Paths.get(uploadDir).resolve(sanitizedFilename);
    }

    /**
     * Deletes a file from storage
     * @param filename the filename to delete
     */
    public void deleteFile(String filename) {
        try {
            String sanitizedFilename = sanitizeFilename(filename);
            Path filePath = Paths.get(uploadDir).resolve(sanitizedFilename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but don't throw - file cleanup is not critical
        }
    }

    /**
     * Checks if a file exists
     * @param filename the filename to check
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filename) {
        String sanitizedFilename = sanitizeFilename(filename);
        Path filePath = Paths.get(uploadDir).resolve(sanitizedFilename);
        return Files.exists(filePath);
    }

    /**
     * Sanitizes filename to prevent path traversal attacks
     * @param filename the filename to sanitize
     * @return sanitized filename
     */
    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new InvalidFileFormatException("Filename cannot be empty");
        }
        
        // Remove any path separators and parent directory references
        filename = filename.replaceAll("[/\\\\]", "");
        filename = filename.replaceAll("\\.\\.", "");
        
        // Only allow alphanumeric, dots, and hyphens
        filename = filename.replaceAll("[^a-zA-Z0-9._-]", "");
        
        if (filename.isEmpty()) {
            throw new InvalidFileFormatException("Invalid filename after sanitization");
        }
        
        return filename;
    }

    /**
     * Validates file before saving
     * @param file the file to validate
     * @throws InvalidFileFormatException if file is invalid
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileFormatException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileFormatException("File size exceeds 5MB limit");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new InvalidFileFormatException("Only PDF, DOC, and DOCX files are allowed");
        }
    }

    /**
     * Extracts file extension from filename
     * @param filename the filename
     * @return the file extension
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Cleans up orphaned resume files (files not referenced in database)
     * This method should be called periodically to maintain storage
     * @param validFilenames set of valid filenames currently in use
     */
    public void cleanupOrphanedFiles(Set<String> validFilenames) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                return;
            }

            Files.list(uploadPath)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        String filename = filePath.getFileName().toString();
                        if (!validFilenames.contains(filename)) {
                            try {
                                Files.delete(filePath);
                            } catch (IOException e) {
                                // Log error but continue cleanup
                            }
                        }
                    });
        } catch (IOException e) {
            // Log error but don't throw - cleanup is not critical
        }
    }
}
