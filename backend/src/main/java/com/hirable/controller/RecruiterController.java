package com.hirable.controller;

import com.hirable.dto.RecruiterProfileDTO;
import com.hirable.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin(origins = "http://localhost:4200")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<RecruiterProfileDTO> getProfile(@PathVariable Long id) {
        RecruiterProfileDTO profile = recruiterService.getProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<RecruiterProfileDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody RecruiterProfileDTO profileDTO) {
        RecruiterProfileDTO updated = recruiterService.updateProfile(id, profileDTO);
        return ResponseEntity.ok(updated);
    }
}
