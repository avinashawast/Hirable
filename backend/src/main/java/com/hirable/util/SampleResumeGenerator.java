package com.hirable.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class SampleResumeGenerator {
    @Value("${hirable.file.upload-dir:./uploads/resumes}")
    private String uploadDir;

    private static final String[][] RESUMES = {
        {
            "resume_001.pdf",
            "JOHN SEEKER\nSan Francisco, CA | john.seeker@email.com | +1-555-0201\n\n" +
            "PROFESSIONAL SUMMARY\nExperienced full-stack developer with 5 years of experience in web development.\n\n" +
            "SKILLS\nJava, JavaScript, React, Spring Boot, PostgreSQL, Docker, AWS, REST API\n\n" +
            "EXPERIENCE\nTech Startup Inc - Full Stack Developer (2019-Present)\nDeveloped and maintained web applications using React and Spring Boot\n\n" +
            "Web Solutions Ltd - Junior Developer (2018-2019)\nBuilt responsive web interfaces and backend APIs\n\n" +
            "EDUCATION\nState University - Bachelor of Science in Computer Science (2018)"
        },
        {
            "resume_002.pdf",
            "JANE DEVELOPER\nNew York, NY | jane.developer@email.com | +1-555-0202\n\n" +
            "PROFESSIONAL SUMMARY\nSenior software engineer specializing in cloud architecture with 7 years of experience.\n\n" +
            "SKILLS\nPython, AWS, Docker, Kubernetes, System Architecture, Microservices, DevOps\n\n" +
            "EXPERIENCE\nCloud Systems Corp - Senior Engineer (2020-Present)\nArchitected cloud-based solutions and led team of 5 engineers\n\n" +
            "Software House - Software Engineer (2017-2020)\nDeveloped enterprise applications\n\n" +
            "EDUCATION\nTech Institute - Master of Science in Computer Science (2017)"
        },
        {
            "resume_003.pdf",
            "MIKE ENGINEER\nAustin, TX | mike.engineer@email.com | +1-555-0203\n\n" +
            "PROFESSIONAL SUMMARY\nBackend developer with expertise in microservices and DevOps.\n\n" +
            "SKILLS\nJava, Spring Boot, Microservices, DevOps, Docker, Kubernetes, CI/CD\n\n" +
            "EXPERIENCE\nMicroservices Ltd - Backend Developer (2019-Present)\nBuilt microservices architecture and DevOps pipelines\n\n" +
            "EDUCATION\nUniversity of Technology - Bachelor of Science in Software Engineering (2019)"
        },
        {
            "resume_004.pdf",
            "SARAH ANALYST\nSeattle, WA | sarah.analyst@email.com | +1-555-0204\n\n" +
            "PROFESSIONAL SUMMARY\nData scientist with machine learning expertise and 4 years of experience.\n\n" +
            "SKILLS\nPython, Machine Learning, Data Analysis, SQL, Tableau, TensorFlow\n\n" +
            "EXPERIENCE\nData Analytics Co - Data Scientist (2020-Present)\nDeveloped machine learning models for predictive analytics\n\n" +
            "EDUCATION\nData Science Academy - Master of Science in Data Science (2020)"
        },
        {
            "resume_005.pdf",
            "ALEX DESIGNER\nLos Angeles, CA | alex.designer@email.com | +1-555-0205\n\n" +
            "PROFESSIONAL SUMMARY\nUI/UX designer with 3 years of experience in product design.\n\n" +
            "SKILLS\nUI/UX Design, Figma, Adobe XD, HTML, CSS, Responsive Design, Accessibility\n\n" +
            "EXPERIENCE\nDesign Studio - UI/UX Designer (2021-Present)\nDesigned user interfaces for mobile and web applications\n\n" +
            "EDUCATION\nDesign School - Bachelor of Arts in Graphic Design (2021)"
        },
        {
            "resume_006.pdf",
            "EMMA MANAGER\nChicago, IL | emma.manager@email.com | +1-555-0206\n\n" +
            "PROFESSIONAL SUMMARY\nProduct manager with background in tech startups and 4 years of experience.\n\n" +
            "SKILLS\nProduct Management, Project Management, Agile, Leadership, Communication\n\n" +
            "EXPERIENCE\nProduct Ventures - Product Manager (2019-Present)\nManaged product roadmap and led cross-functional teams\n\n" +
            "EDUCATION\nBusiness University - MBA in Business Administration (2019)"
        },
        {
            "resume_007.pdf",
            "CHRIS ARCHITECT\nBoston, MA | chris.architect@email.com | +1-555-0207\n\n" +
            "PROFESSIONAL SUMMARY\nSolutions architect specializing in enterprise systems with 6 years of experience.\n\n" +
            "SKILLS\nSystem Architecture, Java, AWS, Database Design, API Design, Microservices\n\n" +
            "EXPERIENCE\nEnterprise Solutions - Solutions Architect (2018-Present)\nDesigned enterprise system architectures\n\n" +
            "EDUCATION\nEngineering College - Master of Science in Computer Engineering (2018)"
        },
        {
            "resume_008.pdf",
            "LISA SPECIALIST\nDenver, CO | lisa.specialist@email.com | +1-555-0208\n\n" +
            "PROFESSIONAL SUMMARY\nQA specialist with automation testing expertise and 3 years of experience.\n\n" +
            "SKILLS\nAutomation Testing, Java, SQL, Problem Solving, Test Frameworks, CI/CD\n\n" +
            "EXPERIENCE\nQA Services - QA Automation Engineer (2020-Present)\nDeveloped automated testing frameworks\n\n" +
            "EDUCATION\nQuality Institute - Bachelor of Science in Information Technology (2020)"
        },
        {
            "resume_009.pdf",
            "DAVID LEAD\nMiami, FL | david.lead@email.com | +1-555-0209\n\n" +
            "PROFESSIONAL SUMMARY\nTechnical lead with team management experience and 5 years of experience.\n\n" +
            "SKILLS\nJava, Leadership, Agile, Spring Boot, Communication, Team Management\n\n" +
            "EXPERIENCE\nTech Company - Technical Lead (2019-Present)\nLed development team and managed technical projects\n\n" +
            "EDUCATION\nTech University - Bachelor of Science in Computer Science (2017)"
        },
        {
            "resume_010.pdf",
            "RACHEL CONSULTANT\nPortland, OR | rachel.consultant@email.com | +1-555-0210\n\n" +
            "PROFESSIONAL SUMMARY\nBusiness analyst and consultant with 4 years of experience.\n\n" +
            "SKILLS\nProject Management, Communication, Problem Solving, Critical Thinking, Excel\n\n" +
            "EXPERIENCE\nConsulting Firm - Business Analyst (2019-Present)\nAnalyzed business requirements and designed solutions\n\n" +
            "EDUCATION\nBusiness School - Bachelor of Business in Business Administration (2019)"
        }
    };

    public void generateSampleResumes() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        for (String[] resume : RESUMES) {
            String filename = resume[0];
            String content = resume[1];
            createPdfResume(uploadPath.resolve(filename), content);
        }
    }

    private void createPdfResume(Path filePath, String content) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                contentStream.beginText();
                contentStream.setTextMatrix(1, 0, 0, 1, 50, 750);

                String[] lines = content.split("\n");
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.endText();
            }

            document.save(filePath.toFile());
        }
    }
}
