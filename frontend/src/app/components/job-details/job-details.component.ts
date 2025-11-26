import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { JobService, JobDTO } from '../../services/job.service';
import { JobSeekerService } from '../../services/job-seeker.service';

@Component({
  selector: 'app-job-details',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './job-details.component.html',
  styleUrls: ['./job-details.component.css']
})
export class JobDetailsComponent implements OnInit {
  job: JobDTO | null = null;
  loading: boolean = true;
  applying: boolean = false;
  jobId: number = 0;
  jobSeekerId: number = 0;
  hasApplied: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobService: JobService,
    private jobSeekerService: JobSeekerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.jobId = params['id'];
      this.loadJobDetails();
      this.checkIfApplied();
    });

    // Get jobSeekerId from localStorage
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const user = JSON.parse(userStr);
      this.jobSeekerId = user.id;
    }
  }

  loadJobDetails(): void {
    this.jobService.getJobById(this.jobId).subscribe({
      next: (job) => {
        this.job = job;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading job details:', error);
        this.loading = false;
        this.snackBar.open('Failed to load job details', 'Close', { duration: 3000 });
      }
    });
  }

  checkIfApplied(): void {
    if (this.jobSeekerId) {
      this.jobSeekerService.getApplications(this.jobSeekerId).subscribe({
        next: (applications) => {
          this.hasApplied = applications.some(app => app.jobId === this.jobId);
        },
        error: (error) => {
          console.error('Error checking applications:', error);
        }
      });
    }
  }

  applyForJob(): void {
    if (!this.jobSeekerId) {
      this.snackBar.open('Please log in to apply', 'Close', { duration: 3000 });
      this.router.navigate(['/login']);
      return;
    }

    this.applying = true;
    this.jobSeekerService.applyToJob(this.jobSeekerId, this.jobId).subscribe({
      next: () => {
        this.hasApplied = true;
        this.applying = false;
        this.snackBar.open('Application submitted successfully!', 'Close', { duration: 3000 });
      },
      error: (error) => {
        this.applying = false;
        console.error('Error applying for job:', error);
        this.snackBar.open('Failed to submit application', 'Close', { duration: 3000 });
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/job-search']);
  }
}
