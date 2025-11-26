import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { JobService, JobDTO, JobStatus } from '../../services/job.service';

@Component({
  selector: 'app-job-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatIconModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule
  ],
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.css']
})
export class JobManagementComponent implements OnInit {
  jobs: JobDTO[] = [];
  filteredJobs: JobDTO[] = [];
  statusFilter = '';
  searchQuery = '';
  showRejectForm = false;
  selectedJobForReject: JobDTO | null = null;
  rejectionReason = '';

  statusOptions = [
    { value: '', label: 'All Status' },
    { value: 'PENDING', label: 'Pending' },
    { value: 'APPROVED', label: 'Approved' },
    { value: 'REJECTED', label: 'Rejected' },
    { value: 'REMOVED', label: 'Removed' }
  ];

  displayedColumns: string[] = ['title', 'recruiter', 'location', 'status', 'postedAt', 'actions'];

  constructor(
    private jobService: JobService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.jobService.getAllJobs().subscribe({
      next: (data) => {
        this.jobs = data;
        this.applyFilters();
      },
      error: (error) => {
        this.snackBar.open('Failed to load jobs', 'Close', { duration: 3000 });
        console.error('Error loading jobs:', error);
      }
    });
  }

  applyFilters(): void {
    this.filteredJobs = this.jobs.filter(job => {
      const matchesSearch = job.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                           job.description.toLowerCase().includes(this.searchQuery.toLowerCase());
      const matchesStatus = !this.statusFilter || job.status === this.statusFilter;
      return matchesSearch && matchesStatus;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onStatusFilterChange(): void {
    this.applyFilters();
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
        return 'primary';
    }
  }

  approveJob(job: JobDTO): void {
    if (confirm(`Are you sure you want to approve "${job.title}"?`)) {
      this.jobService.approveJob(job.id!).subscribe({
        next: () => {
          this.snackBar.open('Job approved successfully', 'Close', { duration: 3000 });
          this.loadJobs();
        },
        error: (error) => {
          this.snackBar.open('Failed to approve job', 'Close', { duration: 3000 });
          console.error('Error approving job:', error);
        }
      });
    }
  }

  openRejectForm(job: JobDTO): void {
    this.selectedJobForReject = job;
    this.rejectionReason = '';
    this.showRejectForm = true;
  }

  closeRejectForm(): void {
    this.showRejectForm = false;
    this.selectedJobForReject = null;
    this.rejectionReason = '';
  }

  rejectJob(): void {
    if (!this.selectedJobForReject) return;

    if (!this.rejectionReason.trim()) {
      this.snackBar.open('Please provide a rejection reason', 'Close', { duration: 3000 });
      return;
    }

    this.jobService.rejectJob(this.selectedJobForReject.id!, this.rejectionReason).subscribe({
      next: () => {
        this.snackBar.open('Job rejected successfully', 'Close', { duration: 3000 });
        this.closeRejectForm();
        this.loadJobs();
      },
      error: (error) => {
        this.snackBar.open('Failed to reject job', 'Close', { duration: 3000 });
        console.error('Error rejecting job:', error);
      }
    });
  }

  removeJob(job: JobDTO): void {
    if (confirm(`Are you sure you want to remove "${job.title}"?`)) {
      this.jobService.removeJob(job.id!).subscribe({
        next: () => {
          this.snackBar.open('Job removed successfully', 'Close', { duration: 3000 });
          this.loadJobs();
        },
        error: (error) => {
          this.snackBar.open('Failed to remove job', 'Close', { duration: 3000 });
          console.error('Error removing job:', error);
        }
      });
    }
  }

  canApprove(job: JobDTO): boolean {
    return job.status === JobStatus.PENDING;
  }

  canReject(job: JobDTO): boolean {
    return job.status === JobStatus.PENDING;
  }

  canRemove(job: JobDTO): boolean {
    return job.status === JobStatus.APPROVED;
  }
}
