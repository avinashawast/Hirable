package com.hirable.controller;

import com.hirable.dto.AnalyticsDTO;
import com.hirable.dto.FlagRequest;
import com.hirable.dto.TalentPoolProfileDTO;
import com.hirable.dto.TaxonomyDTO;
import com.hirable.service.AnalyticsService;
import com.hirable.service.TalentPoolService;
import com.hirable.service.TaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    @Autowired
    private TalentPoolService talentPoolService;

    @Autowired
    private TaxonomyService taxonomyService;

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnalyticsDTO> getAnalytics() {
        AnalyticsDTO analytics = analyticsService.getAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/resumes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TalentPoolProfileDTO>> getAllResumes() {
        List<TalentPoolProfileDTO> resumes = talentPoolService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TalentPoolProfileDTO>> getAllProfiles() {
        List<TalentPoolProfileDTO> profiles = talentPoolService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/profiles/{jobSeekerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TalentPoolProfileDTO> getProfile(@PathVariable Long jobSeekerId) {
        TalentPoolProfileDTO profile = talentPoolService.getProfileById(jobSeekerId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profiles/{jobSeekerId}/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> flagProfile(@PathVariable Long jobSeekerId, @RequestBody FlagRequest request) {
        talentPoolService.flagProfile(jobSeekerId, request.getReason());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profiles/{jobSeekerId}/unflag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unflagProfile(@PathVariable Long jobSeekerId) {
        talentPoolService.unflagProfile(jobSeekerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profiles/{jobSeekerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeProfile(@PathVariable Long jobSeekerId) {
        talentPoolService.removeProfile(jobSeekerId);
        return ResponseEntity.noContent().build();
    }

    // Category endpoints
    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaxonomyDTO>> getAllCategories() {
        List<TaxonomyDTO> categories = taxonomyService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> getCategoryById(@PathVariable Long id) {
        TaxonomyDTO category = taxonomyService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> createCategory(@RequestBody TaxonomyDTO dto) {
        TaxonomyDTO createdCategory = taxonomyService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> updateCategory(@PathVariable Long id, @RequestBody TaxonomyDTO dto) {
        TaxonomyDTO updatedCategory = taxonomyService.updateCategory(id, dto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        taxonomyService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Industry endpoints
    @GetMapping("/industries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaxonomyDTO>> getAllIndustries() {
        List<TaxonomyDTO> industries = taxonomyService.getAllIndustries();
        return ResponseEntity.ok(industries);
    }

    @GetMapping("/industries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> getIndustryById(@PathVariable Long id) {
        TaxonomyDTO industry = taxonomyService.getIndustryById(id);
        return ResponseEntity.ok(industry);
    }

    @PostMapping("/industries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> createIndustry(@RequestBody TaxonomyDTO dto) {
        TaxonomyDTO createdIndustry = taxonomyService.createIndustry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIndustry);
    }

    @PutMapping("/industries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> updateIndustry(@PathVariable Long id, @RequestBody TaxonomyDTO dto) {
        TaxonomyDTO updatedIndustry = taxonomyService.updateIndustry(id, dto);
        return ResponseEntity.ok(updatedIndustry);
    }

    @DeleteMapping("/industries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        taxonomyService.deleteIndustry(id);
        return ResponseEntity.noContent().build();
    }

    // Skill endpoints
    @GetMapping("/skills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaxonomyDTO>> getAllSkills() {
        List<TaxonomyDTO> skills = taxonomyService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> getSkillById(@PathVariable Long id) {
        TaxonomyDTO skill = taxonomyService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> createSkill(@RequestBody TaxonomyDTO dto) {
        TaxonomyDTO createdSkill = taxonomyService.createSkill(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }

    @PutMapping("/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxonomyDTO> updateSkill(@PathVariable Long id, @RequestBody TaxonomyDTO dto) {
        TaxonomyDTO updatedSkill = taxonomyService.updateSkill(id, dto);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        taxonomyService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    // Chat Moderation endpoints
    @GetMapping("/chats/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<com.hirable.dto.ChatDTO>> getAllChats() {
        List<com.hirable.dto.ChatDTO> chats = talentPoolService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @PutMapping("/chats/{chatId}/flag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> flagChat(@PathVariable Long chatId, @RequestBody java.util.Map<String, String> request) {
        String reason = request.get("reason");
        Long adminId = Long.parseLong(request.get("adminId"));
        talentPoolService.flagChat(chatId, reason, adminId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/chats/{chatId}/unflag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unflagChat(@PathVariable Long chatId, @RequestBody java.util.Map<String, Long> request) {
        Long adminId = request.get("adminId");
        talentPoolService.unflagChat(chatId, adminId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}/disable-chat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUserChat(@PathVariable Long userId, @RequestBody java.util.Map<String, Object> request) {
        Long adminId = ((Number) request.get("adminId")).longValue();
        String reason = (String) request.get("reason");
        talentPoolService.disableUserChat(userId, adminId, reason);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}/enable-chat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUserChat(@PathVariable Long userId, @RequestBody java.util.Map<String, Long> request) {
        Long adminId = request.get("adminId");
        talentPoolService.enableUserChat(userId, adminId);
        return ResponseEntity.noContent().build();
    }
}
