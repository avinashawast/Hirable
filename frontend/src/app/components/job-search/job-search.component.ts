import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { JobService, JobDTO } from '../../services/job.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-job-search',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './job-search.component.html',
  styleUrls: ['./job-search.component.css']
})
export class JobSearchComponent implements OnInit {
  keyword: string = '';
  location: string = '';
  industry: string = '';
  experienceLevel: string = '';
  
  jobs: JobDTO[] = [];
  loading: boolean = false;
  totalElements: number = 0;
  pageSize: number = 20;
  currentPage: number = 0;

  industries = ['Technology', 'Finance', 'Healthcare', 'Retail', 'Manufacturing'];
  experienceLevels = ['ENTRY', 'MID', 'SENIOR'];

  constructor(
    private jobService: JobService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.searchJobs();
  }

  searchJobs(): void {
    this.loading = true;
    this.jobService.searchJobs(
      this.keyword,
      this.location,
      this.industry,
      this.experienceLevel,
      this.currentPage
    ).subscribe({
      next: (response) => {
        this.jobs = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching jobs:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.searchJobs();
  }

  viewJobDetails(jobId: number): void {
    this.router.navigate(['/job-details', jobId]);
  }

  resetFilters(): void {
    this.keyword = '';
    this.location = '';
    this.industry = '';
    this.experienceLevel = '';
    this.currentPage = 0;
    this.searchJobs();
  }
}
