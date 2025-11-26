import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RecruiterService, ShortlistDTO } from '../../services/recruiter.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shortlist',
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
  templateUrl: './shortlist.component.html',
  styleUrls: ['./shortlist.component.css']
})
export class ShortlistComponent implements OnInit {
  shortlistedCandidates: ShortlistDTO[] = [];
  isLoading = true;
  error: string | null = null;
  recruiterId: number = 0;

  constructor(
    private recruiterService: RecruiterService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    const userId = localStorage.getItem('userId');
    this.recruiterId = userId ? parseInt(userId) : 0;
  }

  ngOnInit(): void {
    this.loadShortlist();
  }

  loadShortlist(): void {
    this.isLoading = true;
    this.error = null;

    this.recruiterService.getShortlist(this.recruiterId).subscribe({
      next: (candidates) => {
        this.shortlistedCandidates = candidates;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load shortlist:', error);
        this.error = 'Failed to load shortlist. Please try again.';
        this.isLoading = false;
      }
    });
  }

  removeFromShortlist(candidateId: number): void {
    this.recruiterService.removeFromShortlist(this.recruiterId, candidateId).subscribe({
      next: () => {
        this.shortlistedCandidates = this.shortlistedCandidates.filter(c => c.jobSeekerId !== candidateId);
        this.snackBar.open('Candidate removed from shortlist', 'Close', { duration: 3000 });
      },
      error: (error) => {
        console.error('Failed to remove from shortlist:', error);
        this.snackBar.open('Failed to remove candidate from shortlist', 'Close', { duration: 3000 });
      }
    });
  }

  viewCandidateProfile(candidateId: number): void {
    this.router.navigate(['/candidate-profile', candidateId]);
  }
}
