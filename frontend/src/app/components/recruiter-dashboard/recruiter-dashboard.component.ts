import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { Router } from '@angular/router';
import { JobService, JobDTO, JobStatus } from '../../services/job.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-recruiter-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatSnackBarModule,
    MatChipsModule
  ],
  templateUrl: './recruiter-dashboard.component.html',
  styleUrls: ['./recruiter-dashboard.component.css']
})
export class RecruiterDashboardComponent implements OnInit {
  jobs: JobDTO[] = [];
  loading = false;
  recruiterId: number;
  displayedColumns: string[] = ['title', 'location', 'status', 'postedAt', 'actions'];

  constructor(
    private jobService: JobService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.recruiterId = this.authService.getUserId();
    this.loadJobs();
  }

  loadJobs(): void {
    this.loading = true;
    this.jobService.getRecruiterJobs(this.recruiterId).subscribe({
      next: (data: JobDTO[]) => {
        this.jobs = data;
        this.loading = false;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load jobs', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  getStatusColor(status: JobStatus): string {
    switch (status) {
      case JobStatus.PENDING:
        return 'warn';
      case JobStatus.APPROVED:
        return 'accent';
      case JobStatus.REJECTED:
        return 'warn';
      case JobStatus.REMOVED:
        return 'warn';
      default:
        return '';
    }
  }

  editJob(jobId: number | undefined): void {
    if (jobId) {
      this.router.navigate(['/edit-job', jobId]);
    }
  }

  deleteJob(jobId: number | undefined): void {
    if (jobId && confirm('Are you sure you want to delete this job?')) {
      this.jobService.deleteJob(this.recruiterId, jobId).subscribe({
        next: () => {
          this.snackBar.open('Job deleted successfully', 'Close', { duration: 3000 });
          this.loadJobs();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to delete job', 'Close', { duration: 3000 });
        }
      });
    }
  }

  postNewJob(): void {
    this.router.navigate(['/post-job']);
  }
}
