package com.hirable.controller;

import com.hirable.dto.CandidateSearchCriteria;
import com.hirable.dto.CandidateSearchResult;
import com.hirable.dto.JobSeekerProfileDTO;
import com.hirable.service.CandidateSearchService;
import com.hirable.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:4200")
public class CandidateController {
    @Autowired
    private CandidateSearchService candidateSearchService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @GetMapping("/search")
    public ResponseEntity<List<CandidateSearchResult>> searchCandidates(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String location) {

        CandidateSearchCriteria criteria = CandidateSearchCriteria.builder()
                .skills(skills)
                .experience(experience)
                .location(location)
                .build();

        List<CandidateSearchResult> results = candidateSearchService.searchCandidates(criteria);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{candidateId}/profile")
    public ResponseEntity<JobSeekerProfileDTO> getCandidateProfile(@PathVariable Long candidateId) {
        JobSeekerProfileDTO profile = jobSeekerService.getProfile(candidateId);
        return ResponseEntity.ok(profile);
    }
}
