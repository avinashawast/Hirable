package com.hirable.controller;

import com.hirable.dto.ShortlistDTO;
import com.hirable.service.ShortlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin(origins = "http://localhost:4200")
public class ShortlistController {
    @Autowired
    private ShortlistService shortlistService;

    @GetMapping("/{id}/shortlist")
    public ResponseEntity<List<ShortlistDTO>> getShortlist(@PathVariable Long id) {
        List<ShortlistDTO> shortlist = shortlistService.getShortlist(id);
        return ResponseEntity.ok(shortlist);
    }

    @PostMapping("/{id}/shortlist")
    public ResponseEntity<ShortlistDTO> addToShortlist(
            @PathVariable Long id,
            @RequestParam Long candidateId) {
        ShortlistDTO shortlist = shortlistService.addToShortlist(id, candidateId);
        return ResponseEntity.ok(shortlist);
    }

    @DeleteMapping("/{id}/shortlist/{candidateId}")
    public ResponseEntity<Void> removeFromShortlist(
            @PathVariable Long id,
            @PathVariable Long candidateId) {
        shortlistService.removeFromShortlist(id, candidateId);
        return ResponseEntity.noContent().build();
    }
}
