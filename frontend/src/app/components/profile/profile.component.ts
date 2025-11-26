import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
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
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../../services/auth.service';
import { JobSeekerService, JobSeekerProfileDTO, ExperienceDTO, EducationDTO } from '../../services/job-seeker.service';
import { TaxonomyService, TaxonomyDTO } from '../../services/taxonomy.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
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
    MatSelectModule,
    MatTooltipModule
  ],
  template: `
    <div class="profile-container">
      <mat-card class="profile-header-card">
        <mat-card-header>
          <mat-card-title>Job Seeker Profile</mat-card-title>
          <mat-card-subtitle>Manage your professional information</mat-card-subtitle>
        </mat-card-header>
      </mat-card>

      <div *ngIf="isLoading" class="loading">
        <mat-spinner diameter="40"></mat-spinner>
        <p>Loading your profile...</p>
      </div>

      <form [formGroup]="profileForm" *ngIf="!isLoading" class="profile-form">
        <mat-tab-group class="profile-tabs">
          <!-- Personal Information Tab -->
          <mat-tab label="Personal Information">
            <div class="tab-content">
              <mat-card class="section-card">
                <mat-card-header>
                  <mat-card-title>Basic Information</mat-card-title>
                </mat-card-header>
                <mat-card-content>
                  <div class="form-field-row form-field-row-2">
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>First Name</mat-label>
                      <input matInput formControlName="firstName" required>
                    </mat-form-field>
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>Last Name</mat-label>
                      <input matInput formControlName="lastName" required>
                    </mat-form-field>
                  </div>
                  <div class="form-field-row form-field-row-2">
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>Phone</mat-label>
                      <input matInput formControlName="phone">
                    </mat-form-field>
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>Location</mat-label>
                      <input matInput formControlName="location">
                    </mat-form-field>
                  </div>
                </mat-card-content>
              </mat-card>

              <mat-card class="section-card">
                <mat-card-header>
                  <mat-card-title>Professional Summary</mat-card-title>
                </mat-card-header>
                <mat-card-content>
                  <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Summary</mat-label>
                    <textarea matInput formControlName="summary" rows="4" placeholder="Tell us about yourself..."></textarea>
                  </mat-form-field>
                </mat-card-content>
              </mat-card>
            </div>
          </mat-tab>

          <!-- Skills Tab -->
          <mat-tab label="Skills">
            <div class="tab-content">
              <mat-card class="section-card">
                <mat-card-header>
                  <mat-card-title>Your Skills</mat-card-title>
                </mat-card-header>
                <mat-card-content>
                  <div class="skills-section">
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>Select Skill</mat-label>
                      <mat-select [(ngModel)]="selectedSkillInput" [ngModelOptions]="{standalone: true}">
                        <mat-option *ngFor="let skill of availableSkills" [value]="skill.name">
                          {{ skill.name }}
                        </mat-option>
                      </mat-select>
                    </mat-form-field>
                    <button mat-raised-button color="primary" (click)="addSkill(selectedSkillInput)" class="add-skill-btn">
                      <mat-icon>add</mat-icon>
                      Add Skill
                    </button>
                  </div>
                  <div class="skills-list" *ngIf="skills.length > 0">
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
                  <p *ngIf="skills.length === 0" class="empty-state">No skills added yet. Add your first skill above.</p>
                </mat-card-content>
              </mat-card>
            </div>
          </mat-tab>

          <!-- Experience Tab -->
          <mat-tab label="Experience">
            <div class="tab-content">
              <div class="section-header">
                <h3>Work Experience</h3>
                <button mat-raised-button color="primary" (click)="addExperience()">
                  <mat-icon>add</mat-icon>
                  Add Experience
                </button>
              </div>
              <div formArrayName="experiences" class="experiences-list">
                <mat-card *ngFor="let exp of experiences.controls; let i = index" class="section-card experience-card">
                  <mat-card-content [formGroupName]="i">
                    <button mat-icon-button (click)="removeExperience(i)" class="remove-btn" matTooltip="Remove experience">
                      <mat-icon>delete</mat-icon>
                    </button>
                    <div class="form-field-row form-field-row-2">
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Company</mat-label>
                        <input matInput formControlName="company" required>
                      </mat-form-field>
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Job Title</mat-label>
                        <input matInput formControlName="title" required>
                      </mat-form-field>
                    </div>
                    <div class="form-field-row form-field-row-2">
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Start Date</mat-label>
                        <input matInput [matDatepicker]="startPicker" formControlName="startDate">
                        <mat-datepicker-toggle matSuffix [for]="startPicker"></mat-datepicker-toggle>
                        <mat-datepicker #startPicker></mat-datepicker>
                      </mat-form-field>
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>End Date</mat-label>
                        <input matInput [matDatepicker]="endPicker" formControlName="endDate">
                        <mat-datepicker-toggle matSuffix [for]="endPicker"></mat-datepicker-toggle>
                        <mat-datepicker #endPicker></mat-datepicker>
                      </mat-form-field>
                    </div>
                    <mat-form-field appearance="outline" class="full-width">
                      <mat-label>Description</mat-label>
                      <textarea matInput formControlName="description" rows="3" placeholder="Describe your responsibilities and achievements..."></textarea>
                    </mat-form-field>
                  </mat-card-content>
                </mat-card>
              </div>
              <p *ngIf="experiences.length === 0" class="empty-state">No experience added yet. Add your first experience above.</p>
            </div>
          </mat-tab>

          <!-- Education Tab -->
          <mat-tab label="Education">
            <div class="tab-content">
              <div class="section-header">
                <h3>Education</h3>
                <button mat-raised-button color="primary" (click)="addEducation()">
                  <mat-icon>add</mat-icon>
                  Add Education
                </button>
              </div>
              <div formArrayName="educations" class="educations-list">
                <mat-card *ngFor="let edu of educations.controls; let i = index" class="section-card education-card">
                  <mat-card-content [formGroupName]="i">
                    <button mat-icon-button (click)="removeEducation(i)" class="remove-btn" matTooltip="Remove education">
                      <mat-icon>delete</mat-icon>
                    </button>
                    <div class="form-field-row form-field-row-2">
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Institution</mat-label>
                        <input matInput formControlName="institution" required>
                      </mat-form-field>
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Degree</mat-label>
                        <input matInput formControlName="degree" required>
                      </mat-form-field>
                    </div>
                    <div class="form-field-row form-field-row-2">
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Field of Study</mat-label>
                        <input matInput formControlName="fieldOfStudy">
                      </mat-form-field>
                      <mat-form-field appearance="outline" class="full-width">
                        <mat-label>Graduation Date</mat-label>
                        <input matInput [matDatepicker]="gradPicker" formControlName="graduationDate">
                        <mat-datepicker-toggle matSuffix [for]="gradPicker"></mat-datepicker-toggle>
                        <mat-datepicker #gradPicker></mat-datepicker>
                      </mat-form-field>
                    </div>
                  </mat-card-content>
                </mat-card>
              </div>
              <p *ngIf="educations.length === 0" class="empty-state">No education added yet. Add your first education above.</p>
            </div>
          </mat-tab>
        </mat-tab-group>

        <div class="form-actions">
          <button mat-raised-button (click)="cancelEdit()">Cancel</button>
          <button mat-raised-button color="primary" (click)="saveProfile()" [disabled]="isSaving">
            <span *ngIf="!isSaving">Save Profile</span>
            <mat-spinner *ngIf="isSaving" diameter="20"></mat-spinner>
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .profile-container {
      padding: var(--spacing-lg);
      max-width: 900px;
      margin: 0 auto;
    }

    .profile-header-card {
      margin-bottom: var(--spacing-lg);
      background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-secondary) 100%);
      color: white;

      mat-card-header {
        border-bottom: none;
      }

      mat-card-title {
        color: white;
        margin: 0;
      }

      mat-card-subtitle {
        color: rgba(255, 255, 255, 0.9);
        margin-top: var(--spacing-sm);
      }
    }

    .profile-form {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-lg);
    }

    .profile-tabs {
      margin-bottom: var(--spacing-lg);
    }

    .tab-content {
      padding: var(--spacing-lg) 0;
      display: flex;
      flex-direction: column;
      gap: var(--spacing-lg);
    }

    .section-card {
      background-color: white;
      border-radius: var(--border-radius-lg);
      box-shadow: var(--shadow-sm);
      transition: all var(--animation-duration-standard) var(--animation-easing-ease-in-out);

      &:hover {
        box-shadow: var(--shadow-md);
      }

      mat-card-header {
        margin-bottom: var(--spacing-md);
        border-bottom: 1px solid var(--color-neutral-200);
      }

      mat-card-title {
        font-size: var(--font-size-title-large);
        line-height: var(--line-height-title-large);
        color: var(--color-neutral-900);
        margin: 0;
      }

      mat-card-content {
        padding: 0;
      }
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--spacing-lg);
      padding: var(--spacing-md);
      background-color: var(--color-neutral-50);
      border-radius: var(--border-radius-lg);

      h3 {
        margin: 0;
        font-size: var(--font-size-title-large);
        line-height: var(--line-height-title-large);
        color: var(--color-neutral-900);
        font-weight: var(--font-weight-medium);
      }

      button {
        flex-shrink: 0;
      }
    }

    .full-width {
      width: 100%;
    }

    .form-field-row {
      display: grid;
      grid-template-columns: 1fr;
      gap: var(--spacing-md);
      margin-bottom: var(--spacing-md);

      &.form-field-row-2 {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .skills-section {
      display: flex;
      gap: var(--spacing-md);
      margin-bottom: var(--spacing-lg);
      align-items: flex-end;

      mat-form-field {
        flex: 1;
      }

      .add-skill-btn {
        flex-shrink: 0;
        min-width: 120px;
      }
    }

    .skills-list {
      display: flex;
      flex-wrap: wrap;
      gap: var(--spacing-sm);
      margin-top: var(--spacing-md);
    }

    .experiences-list,
    .educations-list {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-lg);
    }

    .experience-card,
    .education-card {
      position: relative;
      padding: var(--spacing-md);

      .remove-btn {
        position: absolute;
        top: var(--spacing-md);
        right: var(--spacing-md);
        color: var(--color-error);

        &:hover {
          background-color: rgba(244, 67, 54, 0.1);
        }
      }

      mat-card-content {
        padding-top: var(--spacing-md);
      }
    }

    .empty-state {
      text-align: center;
      color: var(--color-neutral-600);
      font-size: var(--font-size-body-medium);
      line-height: var(--line-height-body-medium);
      padding: var(--spacing-lg);
      background-color: var(--color-neutral-50);
      border-radius: var(--border-radius-lg);
      margin: var(--spacing-md) 0;
    }

    .form-actions {
      display: flex;
      gap: var(--spacing-md);
      margin-top: var(--spacing-lg);
      padding-top: var(--spacing-lg);
      border-top: 1px solid var(--color-neutral-200);
      justify-content: flex-end;
      flex-wrap: wrap;
    }

    .loading {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      min-height: 400px;
      gap: var(--spacing-md);

      p {
        color: var(--color-neutral-600);
        font-size: var(--font-size-body-medium);
        line-height: var(--line-height-body-medium);
        margin: 0;
      }
    }

    @media (max-width: 599px) {
      .profile-container {
        padding: var(--spacing-md);
      }

      .form-field-row.form-field-row-2 {
        grid-template-columns: 1fr;
      }

      .section-header {
        flex-direction: column;
        align-items: flex-start;
        gap: var(--spacing-md);

        button {
          width: 100%;
        }
      }

      .skills-section {
        flex-direction: column;
        align-items: stretch;

        .add-skill-btn {
          width: 100%;
        }
      }

      .form-actions {
        flex-direction: column;

        button {
          width: 100%;
        }
      }
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

  cancelEdit(): void {
    this.loadProfile();
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
