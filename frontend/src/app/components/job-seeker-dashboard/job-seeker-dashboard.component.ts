import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JobSeekerService, ApplicationDTO } from '../../services/job-seeker.service';
import { NotificationComponent } from '../notification/notification.component';

@Component({
  selector: 'app-job-seeker-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    NotificationComponent
  ],
  templateUrl: './job-seeker-dashboard.component.html',
  styleUrls: ['./job-seeker-dashboard.component.css']
})
export class JobSeekerDashboardComponent implements OnInit {
  applications: ApplicationDTO[] = [];
  loading: boolean = true;
  jobSeekerId: number = 0;

  constructor(
    private jobSeekerService: JobSeekerService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const userIdStr = localStorage.getItem('user_id');
    if (userIdStr) {
      this.jobSeekerId = parseInt(userIdStr, 10);
      this.loadApplications();
    } else {
      this.router.navigate(['/login']);
    }
  }

  loadApplications(): void {
    this.loading = true;
    this.jobSeekerService.getApplications(this.jobSeekerId).subscribe({
      next: (applications) => {
        this.applications = applications;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading applications:', error);
        this.loading = false;
        this.snackBar.open('Failed to load applications', 'Close', { duration: 3000 });
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'APPLIED':
        return 'primary';
      case 'SHORTLISTED':
        return 'accent';
      case 'REJECTED':
        return 'warn';
      default:
        return 'primary';
    }
  }

  searchMoreJobs(): void {
    this.router.navigate(['/job-search']);
  }
}
