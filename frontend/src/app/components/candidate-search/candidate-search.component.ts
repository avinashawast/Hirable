import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CandidateService, CandidateSearchResult } from '../../services/candidate.service';
import { RecruiterService } from '../../services/recruiter.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-candidate-search',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './candidate-search.component.html',
  styleUrls: ['./candidate-search.component.css']
})
export class CandidateSearchComponent implements OnInit {
  searchResults: CandidateSearchResult[] = [];
  isLoading = false;
  hasSearched = false;
  shortlistedCandidates: Set<number> = new Set();
  recruiterId: number = 0;

  // Search filters
  skillsInput = '';
  selectedSkills: string[] = [];
  experience = '';
  location = '';

  experienceLevels = ['ENTRY', 'MID', 'SENIOR'];

  constructor(
    private candidateService: CandidateService,
    private recruiterService: RecruiterService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    const userId = localStorage.getItem('userId');
    this.recruiterId = userId ? parseInt(userId) : 0;
  }

  ngOnInit(): void {
    this.loadShortlistedCandidates();
  }

  loadShortlistedCandidates(): void {
    this.recruiterService.getShortlist(this.recruiterId).subscribe({
      next: (shortlist) => {
        this.shortlistedCandidates = new Set(shortlist.map(s => s.jobSeekerId));
      },
      error: (error) => {
        console.error('Failed to load shortlist:', error);
      }
    });
  }

  addSkill(): void {
    if (this.skillsInput.trim() && !this.selectedSkills.includes(this.skillsInput.trim())) {
      this.selectedSkills.push(this.skillsInput.trim());
      this.skillsInput = '';
    }
  }

  removeSkill(skill: string): void {
    this.selectedSkills = this.selectedSkills.filter(s => s !== skill);
  }

  searchCandidates(): void {
    this.isLoading = true;
    this.hasSearched = true;

    this.candidateService.searchCandidates(
      this.selectedSkills.length > 0 ? this.selectedSkills : undefined,
      this.experience || undefined,
      this.location || undefined
    ).subscribe({
      next: (results) => {
        this.searchResults = results;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Search failed:', error);
        this.isLoading = false;
      }
    });
  }

  viewCandidateProfile(candidateId: number): void {
    this.router.navigate(['/candidate-profile', candidateId]);
  }

  clearFilters(): void {
    this.selectedSkills = [];
    this.skillsInput = '';
    this.experience = '';
    this.location = '';
    this.searchResults = [];
    this.hasSearched = false;
  }

  getRelevancePercentage(score: number): number {
    return Math.round(score * 100);
  }

  toggleShortlist(candidateId: number): void {
    if (this.shortlistedCandidates.has(candidateId)) {
      this.recruiterService.removeFromShortlist(this.recruiterId, candidateId).subscribe({
        next: () => {
          this.shortlistedCandidates.delete(candidateId);
          this.snackBar.open('Candidate removed from shortlist', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Failed to remove from shortlist:', error);
          this.snackBar.open('Failed to remove candidate from shortlist', 'Close', { duration: 3000 });
        }
      });
    } else {
      this.recruiterService.addToShortlist(this.recruiterId, candidateId).subscribe({
        next: () => {
          this.shortlistedCandidates.add(candidateId);
          this.snackBar.open('Candidate added to shortlist', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Failed to add to shortlist:', error);
          this.snackBar.open('Failed to add candidate to shortlist', 'Close', { duration: 3000 });
        }
      });
    }
  }

  isShortlisted(candidateId: number): boolean {
    return this.shortlistedCandidates.has(candidateId);
  }
}
