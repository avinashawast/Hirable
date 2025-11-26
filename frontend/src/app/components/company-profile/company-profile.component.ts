import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RecruiterService, RecruiterProfileDTO } from '../../services/recruiter.service';
import { AuthService } from '../../services/auth.service';
import { TaxonomyService, TaxonomyDTO } from '../../services/taxonomy.service';

@Component({
  selector: 'app-company-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.css']
})
export class CompanyProfileComponent implements OnInit {
  profile: RecruiterProfileDTO = {
    firstName: '',
    lastName: '',
    phone: '',
    companyName: '',
    companyWebsite: '',
    companyDescription: '',
    industry: ''
  };

  loading = false;
  recruiterId: number = 0;
  industries: TaxonomyDTO[] = [];

  constructor(
    private recruiterService: RecruiterService,
    private authService: AuthService,
    private taxonomyService: TaxonomyService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.recruiterId = this.authService.getUserId() || 0;
    this.loadIndustries();
    this.loadProfile();
  }

  loadIndustries(): void {
    this.taxonomyService.getAllIndustries().subscribe({
      next: (data: any) => {
        this.industries = data;
      },
      error: (error: any) => {
        console.error('Error loading industries:', error);
      }
    });
  }

  loadProfile(): void {
    this.loading = true;
    this.recruiterService.getProfile(this.recruiterId).subscribe({
      next: (data: RecruiterProfileDTO) => {
        this.profile = data;
        this.loading = false;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load profile', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  saveProfile(): void {
    this.loading = true;
    this.recruiterService.updateProfile(this.recruiterId, this.profile).subscribe({
      next: (data: RecruiterProfileDTO) => {
        this.profile = data;
        this.snackBar.open('Profile updated successfully', 'Close', { duration: 3000 });
        this.loading = false;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to update profile', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
