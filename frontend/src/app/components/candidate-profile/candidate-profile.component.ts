import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CandidateService, CandidateProfile } from '../../services/candidate.service';
import { RecruiterService } from '../../services/recruiter.service';

@Component({
  selector: 'app-candidate-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './candidate-profile.component.html',
  styleUrls: ['./candidate-profile.component.css']
})
export class CandidateProfileComponent implements OnInit {
  candidate: CandidateProfile | null = null;
  isLoading = true;
  error: string | null = null;
  isShortlisted = false;
  recruiterId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private candidateService: CandidateService,
    private recruiterService: RecruiterService,
    private snackBar: MatSnackBar
  ) {
    const userId = localStorage.getItem('userId');
    this.recruiterId = userId ? parseInt(userId) : 0;
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const candidateId = params['id'];
      if (candidateId) {
        this.loadCandidateProfile(candidateId);
      }
    });
  }

  loadCandidateProfile(candidateId: number): void {
    this.isLoading = true;
    this.error = null;

    this.candidateService.getCandidateProfile(candidateId).subscribe({
      next: (profile) => {
        this.candidate = profile;
        this.checkIfShortlisted(candidateId);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load candidate profile:', error);
        this.error = 'Failed to load candidate profile. Please try again.';
        this.isLoading = false;
      }
    });
  }

  checkIfShortlisted(candidateId: number): void {
    this.recruiterService.getShortlist(this.recruiterId).subscribe({
      next: (shortlist) => {
        this.isShortlisted = shortlist.some(s => s.jobSeekerId === candidateId);
      },
      error: (error) => {
        console.error('Failed to check shortlist status:', error);
      }
    });
  }

  downloadResume(): void {
    if (this.candidate && this.candidate.resumeFilePath) {
      const link = document.createElement('a');
      link.href = `http://localhost:8080/api/jobseekers/${this.candidate.id}/resume/download`;
      link.download = `${this.candidate.firstName}_${this.candidate.lastName}_resume`;
      link.click();
    }
  }

  goBack(): void {
    this.router.navigate(['/candidate-search']);
  }

  getRelevancePercentage(score: number): number {
    return Math.round(score * 100);
  }

  toggleShortlist(): void {
    if (!this.candidate) return;

    if (this.isShortlisted) {
      this.recruiterService.removeFromShortlist(this.recruiterId, this.candidate.id).subscribe({
        next: () => {
          this.isShortlisted = false;
          this.snackBar.open('Candidate removed from shortlist', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Failed to remove from shortlist:', error);
          this.snackBar.open('Failed to remove candidate from shortlist', 'Close', { duration: 3000 });
        }
      });
    } else {
      this.recruiterService.addToShortlist(this.recruiterId, this.candidate.id).subscribe({
        next: () => {
          this.isShortlisted = true;
          this.snackBar.open('Candidate added to shortlist', 'Close', { duration: 3000 });
        },
        error: (error) => {
          console.error('Failed to add to shortlist:', error);
          this.snackBar.open('Failed to add candidate to shortlist', 'Close', { duration: 3000 });
        }
      });
    }
  }
}
