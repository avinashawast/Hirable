import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { AuthService } from '../../services/auth.service';
import { JobSeekerService, JobSeekerProfileDTO, ExperienceDTO, EducationDTO } from '../../services/job-seeker.service';
import { TaxonomyService, TaxonomyDTO } from '../../services/taxonomy.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTabsModule,
    MatSelectModule
  ],
  template: `
    <div class="profile-container">
      <mat-card class="profile-card">
        <mat-card-header>
          <mat-card-title>Job Seeker Profile</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div *ngIf="isLoading" class="loading">
            <mat-spinner diameter="40"></mat-spinner>
          </div>
          <form [formGroup]="profileForm" *ngIf="!isLoading">
            <mat-tab-group>
              <!-- Personal Information Tab -->
              <mat-tab label="Personal Information">
                <div class="tab-content">
                  <mat-form-field appearance="fill" class="full-width">
                    <mat-label>First Name</mat-label>
                    <input matInput formControlName="firstName" required>
                  </mat-form-field>
                  <mat-form-field appearance="fill" class="full-width">
                    <mat-label>Last Name</mat-label>
                    <input matInput formControlName="lastName" required>
                  </mat-form-field>
                  <mat-form-field appearance="fill" class="full-width">
                    <mat-label>Phone</mat-label>
                    <input matInput formControlName="phone">
                  </mat-form-field>
                  <mat-form-field appearance="fill" class="full-width">
                    <mat-label>Location</mat-label>
                    <input matInput formControlName="location">
                  </mat-form-field>
                  <mat-form-field appearance="fill" class="full-width">
                    <mat-label>Professional Summary</mat-label>
                    <textarea matInput formControlName="summary" rows="4"></textarea>
                  </mat-form-field>
                </div>
              </mat-tab>

              <!-- Skills Tab -->
              <mat-tab label="Skills">
                <div class="tab-content">
                  <div class="skills-section">
                    <mat-form-field appearance="fill" class="full-width">
                      <mat-label>Select Skill</mat-label>
                      <mat-select [(ngModel)]="selectedSkillInput" [ngModelOptions]="{standalone: true}">
                        <mat-option *ngFor="let skill of availableSkills" [value]="skill.name">
                          {{ skill.name }}
                        </mat-option>
                      </mat-select>
                    </mat-form-field>
                    <button mat-raised-button color="primary" (click)="addSkill(selectedSkillInput)">
                      Add Skill
                    </button>
                  </div>
                  <div class="skills-list">
                    <mat-chip-set aria-label="Skills">
                      <mat-chip 
                        *ngFor="let skill of skills; let i = index"
                        (removed)="removeSkill(i)">
                        {{ skill }}
                        <button matChipRemove>
                          <mat-icon>cancel</mat-icon>
                        </button>
                      </mat-chip>
                    </mat-chip-set>
                  </div>
                </div>
              </mat-tab>

              <!-- Experience Tab -->
              <mat-tab label="Experience">
                <div class="tab-content">
                  <button mat-raised-button color="primary" (click)="addExperience()">
                    Add Experience
                  </button>
                  <div formArrayName="experiences" class="experiences-list">
                    <mat-card *ngFor="let exp of experiences.controls; let i = index" class="experience-card">
                      <mat-card-content [formGroupName]="i">
                        <button mat-icon-button (click)="removeExperience(i)" class="remove-btn">
                          <mat-icon>delete</mat-icon>
                        </button>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Company</mat-label>
                          <input matInput formControlName="company" required>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Job Title</mat-label>
                          <input matInput formControlName="title" required>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Start Date</mat-label>
                          <input matInput [matDatepicker]="startPicker" formControlName="startDate">
                          <mat-datepicker-toggle matSuffix [for]="startPicker"></mat-datepicker-toggle>
                          <mat-datepicker #startPicker></mat-datepicker>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>End Date</mat-label>
                          <input matInput [matDatepicker]="endPicker" formControlName="endDate">
                          <mat-datepicker-toggle matSuffix [for]="endPicker"></mat-datepicker-toggle>
                          <mat-datepicker #endPicker></mat-datepicker>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Description</mat-label>
                          <textarea matInput formControlName="description" rows="3"></textarea>
                        </mat-form-field>
                      </mat-card-content>
                    </mat-card>
                  </div>
                </div>
              </mat-tab>

              <!-- Education Tab -->
              <mat-tab label="Education">
                <div class="tab-content">
                  <button mat-raised-button color="primary" (click)="addEducation()">
                    Add Education
                  </button>
                  <div formArrayName="educations" class="educations-list">
                    <mat-card *ngFor="let edu of educations.controls; let i = index" class="education-card">
                      <mat-card-content [formGroupName]="i">
                        <button mat-icon-button (click)="removeEducation(i)" class="remove-btn">
                          <mat-icon>delete</mat-icon>
                        </button>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Institution</mat-label>
                          <input matInput formControlName="institution" required>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Degree</mat-label>
                          <input matInput formControlName="degree" required>
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Field of Study</mat-label>
                          <input matInput formControlName="fieldOfStudy">
                        </mat-form-field>
                        <mat-form-field appearance="fill" class="full-width">
                          <mat-label>Graduation Date</mat-label>
                          <input matInput [matDatepicker]="gradPicker" formControlName="graduationDate">
                          <mat-datepicker-toggle matSuffix [for]="gradPicker"></mat-datepicker-toggle>
                          <mat-datepicker #gradPicker></mat-datepicker>
                        </mat-form-field>
                      </mat-card-content>
                    </mat-card>
                  </div>
                </div>
              </mat-tab>
            </mat-tab-group>

            <div class="button-group">
              <button mat-raised-button color="primary" (click)="saveProfile()" [disabled]="isSaving">
                <span *ngIf="!isSaving">Save Profile</span>
                <mat-spinner *ngIf="isSaving" diameter="20"></mat-spinner>
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .profile-container {
      padding: 20px;
      max-width: 900px;
      margin: 0 auto;
    }

    .profile-card {
      margin-bottom: 20px;
    }

    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }

    .tab-content {
      padding: 20px 0;
    }

    .skills-section {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }

    .skills-section mat-form-field {
      flex: 1;
    }

    .skills-list {
      margin-top: 20px;
    }

    .experiences-list,
    .educations-list {
      margin-top: 20px;
    }

    .experience-card,
    .education-card {
      margin-bottom: 20px;
      position: relative;
    }

    .remove-btn {
      position: absolute;
      top: 10px;
      right: 10px;
    }

    .button-group {
      display: flex;
      gap: 10px;
      margin-top: 20px;
      justify-content: flex-end;
    }

    .loading {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 400px;
    }
  `]
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isLoading = false;
  isSaving = false;
  skills: string[] = [];
  availableSkills: TaxonomyDTO[] = [];
  selectedSkillInput = '';

  constructor(
    private fb: FormBuilder,
    private jobSeekerService: JobSeekerService,
    private authService: AuthService,
    private taxonomyService: TaxonomyService,
    private snackBar: MatSnackBar
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.loadSkillsTaxonomy();
    this.loadProfile();
  }

  private loadSkillsTaxonomy(): void {
    this.taxonomyService.getAllSkills().subscribe({
      next: (data: any) => {
        this.availableSkills = data;
      },
      error: (error: any) => {
        console.error('Error loading skills taxonomy:', error);
      }
    });
  }

  private initializeForm(): void {
    this.profileForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: [''],
      location: [''],
      summary: [''],
      experiences: this.fb.array([]),
      educations: this.fb.array([])
    });
  }

  get experiences(): FormArray {
    return this.profileForm.get('experiences') as FormArray;
  }

  get educations(): FormArray {
    return this.profileForm.get('educations') as FormArray;
  }

  private loadProfile(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.snackBar.open('User ID not found', 'Close', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    this.jobSeekerService.getProfile(userId).subscribe({
      next: (profile: JobSeekerProfileDTO) => {
        this.populateForm(profile);
        this.isLoading = false;
      },
      error: (error: any) => {
        this.isLoading = false;
        this.snackBar.open('Failed to load profile', 'Close', { duration: 3000 });
      }
    });
  }

  private populateForm(profile: JobSeekerProfileDTO): void {
    this.profileForm.patchValue({
      firstName: profile.firstName,
      lastName: profile.lastName,
      phone: profile.phone,
      location: profile.location,
      summary: profile.summary
    });

    this.skills = profile.skills || [];

    // Populate experiences
    if (profile.experiences && profile.experiences.length > 0) {
      profile.experiences.forEach(exp => {
        this.experiences.push(this.createExperienceFormGroup(exp));
      });
    }

    // Populate educations
    if (profile.educations && profile.educations.length > 0) {
      profile.educations.forEach(edu => {
        this.educations.push(this.createEducationFormGroup(edu));
      });
    }
  }

  private createExperienceFormGroup(exp?: ExperienceDTO): FormGroup {
    return this.fb.group({
      id: [exp?.id],
      company: [exp?.company || '', Validators.required],
      title: [exp?.title || '', Validators.required],
      startDate: [exp?.startDate ? new Date(exp.startDate) : ''],
      endDate: [exp?.endDate ? new Date(exp.endDate) : ''],
      description: [exp?.description || '']
    });
  }

  private createEducationFormGroup(edu?: EducationDTO): FormGroup {
    return this.fb.group({
      id: [edu?.id],
      institution: [edu?.institution || '', Validators.required],
      degree: [edu?.degree || '', Validators.required],
      fieldOfStudy: [edu?.fieldOfStudy || ''],
      graduationDate: [edu?.graduationDate ? new Date(edu.graduationDate) : '']
    });
  }

  addSkill(skill: string): void {
    if (skill && skill.trim() && !this.skills.includes(skill.trim())) {
      this.skills.push(skill.trim());
      this.selectedSkillInput = '';
    }
  }

  removeSkill(index: number): void {
    this.skills.splice(index, 1);
  }

  addExperience(): void {
    this.experiences.push(this.createExperienceFormGroup());
  }

  removeExperience(index: number): void {
    this.experiences.removeAt(index);
  }

  addEducation(): void {
    this.educations.push(this.createEducationFormGroup());
  }

  removeEducation(index: number): void {
    this.educations.removeAt(index);
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      const userId = this.authService.getUserId();
      if (!userId) {
        this.snackBar.open('User ID not found', 'Close', { duration: 3000 });
        return;
      }

      this.isSaving = true;
      const profileData: JobSeekerProfileDTO = {
        ...this.profileForm.value,
        skills: this.skills
      };

      this.jobSeekerService.updateProfile(userId, profileData).subscribe({
        next: (response: JobSeekerProfileDTO) => {
          this.isSaving = false;
          this.snackBar.open('Profile saved successfully!', 'Close', { duration: 3000 });
        },
        error: (error: any) => {
          this.isSaving = false;
          this.snackBar.open('Failed to save profile', 'Close', { duration: 3000 });
        }
      });
    }
  }
}
