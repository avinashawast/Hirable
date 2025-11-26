package com.hirable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_seekers", indexes = {
    @Index(name = "idx_job_seeker_resume_uploaded_at", columnList = "resumeUploadedAt"),
    @Index(name = "idx_job_seeker_location", columnList = "location")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String summary;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "job_seeker_skills", joinColumns = @JoinColumn(name = "job_seeker_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "job_seeker_id")
    @Builder.Default
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "job_seeker_id")
    @Builder.Default
    private List<Education> educations = new ArrayList<>();

    private String resumeFilePath;
    private LocalDateTime resumeUploadedAt;
    @Builder.Default
    private double relevanceScore = 0.0;

    @Builder.Default
    private boolean flagged = false;
    private String flagReason;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
