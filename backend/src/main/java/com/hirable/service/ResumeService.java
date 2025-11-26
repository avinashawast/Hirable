package com.hirable.service;

import com.hirable.dto.EducationDTO;
import com.hirable.dto.ExperienceDTO;
import com.hirable.dto.ParsedResumeData;
import com.hirable.exception.InvalidFileFormatException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeService {
    @Autowired
    private FileStorageService fileStorageService;

    public String saveResumeFile(MultipartFile file) throws IOException {
        return fileStorageService.saveFile(file);
    }

    public void deleteResumeFile(String filename) {
        fileStorageService.deleteFile(filename);
    }

    public ParsedResumeData parseResume(MultipartFile file) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        String content;

        if ("pdf".equals(extension)) {
            content = extractTextFromPdf(file);
        } else if ("doc".equals(extension) || "docx".equals(extension)) {
            content = extractTextFromDoc(file);
        } else if ("txt".equals(extension)) {
            content = new String(file.getBytes());
        } else {
            throw new InvalidFileFormatException("Unsupported file format");
        }

        return extractDataFromContent(content);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        // For now, treat PDF as text file
        // Full PDF parsing requires additional configuration with PDFBox 3.0.0
        return new String(file.getBytes());
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1) : "";
    }

    private String extractTextFromDoc(MultipartFile file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                content.append(paragraph.getText()).append("\n");
            }
        }
        return content.toString();
    }

    private ParsedResumeData extractDataFromContent(String content) {
        List<String> skills = extractSkills(content);
        List<ExperienceDTO> experiences = extractExperiences(content);
        List<EducationDTO> educations = extractEducations(content);

        return ParsedResumeData.builder()
                .skills(skills)
                .experiences(experiences)
                .educations(educations)
                .build();
    }

    private List<String> extractSkills(String content) {
        List<String> skills = new ArrayList<>();
        String lowerContent = content.toLowerCase();

        // Common technical skills
        String[] commonSkills = {
                "java", "python", "javascript", "typescript", "c#", "c++", "go", "rust",
                "spring", "spring boot", "hibernate", "jpa", "react", "angular", "vue",
                "node.js", "express", "django", "flask", "fastapi",
                "sql", "postgresql", "mysql", "mongodb", "redis", "elasticsearch",
                "docker", "kubernetes", "aws", "azure", "gcp",
                "git", "maven", "gradle", "npm", "yarn",
                "junit", "mockito", "pytest", "jest",
                "rest", "graphql", "soap", "microservices",
                "html", "css", "scss", "bootstrap", "material design",
                "agile", "scrum", "kanban", "jira",
                "linux", "windows", "macos", "unix"
        };

        for (String skill : commonSkills) {
            if (lowerContent.contains(skill)) {
                skills.add(skill);
            }
        }

        return skills;
    }

    private List<ExperienceDTO> extractExperiences(String content) {
        List<ExperienceDTO> experiences = new ArrayList<>();

        // Pattern to match experience sections
        Pattern experiencePattern = Pattern.compile(
                "(?i)(experience|work experience|professional experience)\\s*:?\\s*([\\s\\S]*?)(?=education|skills|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = experiencePattern.matcher(content);
        if (matcher.find()) {
            String experienceSection = matcher.group(2);
            String[] lines = experienceSection.split("\n");

            ExperienceDTO currentExperience = null;
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Try to detect company/title line
                if (line.matches(".*[A-Z].*") && !line.matches(".*\\d{4}.*")) {
                    if (currentExperience != null) {
                        experiences.add(currentExperience);
                    }
                    currentExperience = ExperienceDTO.builder()
                            .company(line)
                            .title("")
                            .description("")
                            .build();
                } else if (currentExperience != null) {
                    if (currentExperience.getTitle().isEmpty()) {
                        currentExperience.setTitle(line);
                    } else {
                        currentExperience.setDescription(
                                currentExperience.getDescription() + " " + line
                        );
                    }
                }
            }

            if (currentExperience != null) {
                experiences.add(currentExperience);
            }
        }

        return experiences;
    }

    private List<EducationDTO> extractEducations(String content) {
        List<EducationDTO> educations = new ArrayList<>();

        // Pattern to match education sections
        Pattern educationPattern = Pattern.compile(
                "(?i)(education|academic|degree)\\s*:?\\s*([\\s\\S]*?)(?=experience|skills|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = educationPattern.matcher(content);
        if (matcher.find()) {
            String educationSection = matcher.group(2);
            String[] lines = educationSection.split("\n");

            EducationDTO currentEducation = null;
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Try to detect degree/institution line
                if (line.matches(".*[A-Z].*")) {
                    if (currentEducation != null) {
                        educations.add(currentEducation);
                    }
                    currentEducation = EducationDTO.builder()
                            .degree(line)
                            .institution("")
                            .fieldOfStudy("")
                            .build();
                } else if (currentEducation != null) {
                    if (currentEducation.getInstitution().isEmpty()) {
                        currentEducation.setInstitution(line);
                    } else {
                        currentEducation.setFieldOfStudy(line);
                    }
                }
            }

            if (currentEducation != null) {
                educations.add(currentEducation);
            }
        }

        return educations;
    }

}
