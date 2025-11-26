import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JobService, JobDTO } from '../../services/job.service';
import { AuthService } from '../../services/auth.service';
import { TaxonomyService, TaxonomyDTO } from '../../services/taxonomy.service';

@Component({
  selector: 'app-job-posting-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatChipsModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  templateUrl: './job-posting-form.component.html',
  styleUrls: ['./job-posting-form.component.css']
})
export class JobPostingFormComponent implements OnInit {
  job: JobDTO = {
    title: '',
    description: '',
    requiredSkills: [],
    location: '',
    experienceLevel: '',
    industry: ''
  };

  skillInput = '';
  loading = false;
  recruiterId: number;
  industries: TaxonomyDTO[] = [];
  skills: TaxonomyDTO[] = [];

  constructor(
    private jobService: JobService,
    private authService: AuthService,
    private taxonomyService: TaxonomyService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.recruiterId = this.authService.getUserId();
    this.loadTaxonomies();
  }

  loadTaxonomies(): void {
    this.taxonomyService.getAllIndustries().subscribe({
      next: (data: any) => {
        this.industries = data;
      },
      error: (error: any) => {
        console.error('Error loading industries:', error);
      }
    });

    this.taxonomyService.getAllSkills().subscribe({
      next: (data: any) => {
        this.skills = data;
      },
      error: (error: any) => {
        console.error('Error loading skills:', error);
      }
    });
  }

  addSkill(): void {
    if (this.skillInput.trim()) {
      this.job.requiredSkills.push(this.skillInput.trim());
      this.skillInput = '';
    }
  }

  removeSkill(index: number): void {
    this.job.requiredSkills.splice(index, 1);
  }

  submitForm(): void {
    if (!this.job.title || !this.job.description || !this.job.location) {
      this.snackBar.open('Please fill in all required fields', 'Close', { duration: 3000 });
      return;
    }

    this.loading = true;
    this.jobService.createJob(this.recruiterId, this.job).subscribe({
      next: (data: JobDTO) => {
        this.snackBar.open('Job posted successfully', 'Close', { duration: 3000 });
        this.loading = false;
        this.router.navigate(['/recruiter-dashboard']);
      },
      error: (error: any) => {
        this.snackBar.open('Failed to post job', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
