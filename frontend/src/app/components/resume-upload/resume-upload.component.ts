import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../services/auth.service';
import { JobSeekerService } from '../../services/job-seeker.service';

@Component({
  selector: 'app-resume-upload',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressBarModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  template: `
    <div class="resume-upload-container">
      <mat-card class="upload-card">
        <mat-card-header>
          <mat-card-title>Resume Upload</mat-card-title>
          <mat-card-subtitle>Upload your resume (PDF, DOC, or DOCX - Max 5MB)</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="upload-section">
            <div class="drop-zone" 
              (dragover)="onDragOver($event)"
              (dragleave)="onDragLeave($event)"
              (drop)="onDrop($event)"
              [class.drag-over]="isDragOver">
              <mat-icon class="upload-icon">cloud_upload</mat-icon>
              <p>Drag and drop your resume here or click to select</p>
              <input 
                type="file" 
                #fileInput 
                hidden 
                (change)="onFileSelected($event)"
                accept=".pdf,.doc,.docx">
              <button 
                mat-raised-button 
                color="primary"
                (click)="fileInput.click()"
                [disabled]="isUploading">
                Select File
              </button>
            </div>

            <div *ngIf="selectedFile" class="file-info">
              <mat-icon>description</mat-icon>
              <div class="file-details">
                <p class="file-name">{{ selectedFile.name }}</p>
                <p class="file-size">{{ (selectedFile.size / 1024 / 1024).toFixed(2) }} MB</p>
              </div>
            </div>

            <div *ngIf="isUploading" class="upload-progress">
              <mat-progress-bar mode="indeterminate"></mat-progress-bar>
              <p>Uploading and parsing resume...</p>
            </div>

            <div *ngIf="uploadSuccess" class="success-message">
              <mat-icon>check_circle</mat-icon>
              <p>Resume uploaded successfully!</p>
              <p class="success-details">Your resume has been parsed and your profile has been updated.</p>
            </div>

            <div *ngIf="uploadError" class="error-message">
              <mat-icon>error</mat-icon>
              <p>{{ uploadError }}</p>
            </div>

            <div class="button-group">
              <button 
                mat-raised-button 
                color="primary"
                (click)="uploadResume()"
                [disabled]="!selectedFile || isUploading">
                <span *ngIf="!isUploading">Upload Resume</span>
                <mat-spinner *ngIf="isUploading" diameter="20"></mat-spinner>
              </button>
              <button 
                mat-stroked-button
                (click)="clearSelection()"
                [disabled]="isUploading">
                Clear
              </button>
            </div>
          </div>

          <div class="info-section">
            <h3>Supported Formats</h3>
            <ul>
              <li>PDF (.pdf)</li>
              <li>Microsoft Word (.doc, .docx)</li>
            </ul>
            <h3>What We Extract</h3>
            <ul>
              <li>Skills and competencies</li>
              <li>Work experience</li>
              <li>Education details</li>
            </ul>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .resume-upload-container {
      padding: 20px;
      max-width: 600px;
      margin: 0 auto;
    }

    .upload-card {
      margin-bottom: 20px;
    }

    .upload-section {
      margin: 20px 0;
    }

    .drop-zone {
      border: 2px dashed #ccc;
      border-radius: 8px;
      padding: 40px 20px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      background-color: #f9f9f9;
    }

    .drop-zone:hover {
      border-color: #667eea;
      background-color: #f0f0ff;
    }

    .drop-zone.drag-over {
      border-color: #667eea;
      background-color: #e8e8ff;
    }

    .upload-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      color: #667eea;
      margin-bottom: 10px;
    }

    .drop-zone p {
      margin: 10px 0;
      color: #666;
    }

    .file-info {
      display: flex;
      align-items: center;
      gap: 15px;
      padding: 15px;
      background-color: #f5f5f5;
      border-radius: 8px;
      margin: 20px 0;
    }

    .file-info mat-icon {
      font-size: 32px;
      width: 32px;
      height: 32px;
      color: #667eea;
    }

    .file-details {
      flex: 1;
    }

    .file-name {
      font-weight: 500;
      margin: 0;
      word-break: break-all;
    }

    .file-size {
      font-size: 12px;
      color: #999;
      margin: 5px 0 0 0;
    }

    .upload-progress {
      margin: 20px 0;
    }

    .upload-progress p {
      text-align: center;
      color: #666;
      margin-top: 10px;
    }

    .success-message {
      display: flex;
      align-items: center;
      gap: 15px;
      padding: 15px;
      background-color: #e8f5e9;
      border-radius: 8px;
      margin: 20px 0;
      color: #2e7d32;
    }

    .success-message mat-icon {
      font-size: 32px;
      width: 32px;
      height: 32px;
    }

    .success-details {
      font-size: 12px;
      margin: 5px 0 0 0;
    }

    .error-message {
      display: flex;
      align-items: center;
      gap: 15px;
      padding: 15px;
      background-color: #ffebee;
      border-radius: 8px;
      margin: 20px 0;
      color: #c62828;
    }

    .error-message mat-icon {
      font-size: 32px;
      width: 32px;
      height: 32px;
    }

    .button-group {
      display: flex;
      gap: 10px;
      margin-top: 20px;
      justify-content: center;
    }

    .button-group button {
      flex: 1;
    }

    .info-section {
      margin-top: 30px;
      padding-top: 20px;
      border-top: 1px solid #eee;
    }

    .info-section h3 {
      margin: 15px 0 10px 0;
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }

    .info-section ul {
      margin: 0;
      padding-left: 20px;
      font-size: 13px;
      color: #666;
    }

    .info-section li {
      margin: 5px 0;
    }
  `]
})
export class ResumeUploadComponent implements OnInit {
  selectedFile: File | null = null;
  isUploading = false;
  uploadSuccess = false;
  uploadError: string | null = null;
  isDragOver = false;

  constructor(
    private jobSeekerService: JobSeekerService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;

    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.selectedFile = files[0];
      this.validateFile();
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.validateFile();
    }
  }

  private validateFile(): void {
    if (!this.selectedFile) return;

    const allowedExtensions = ['pdf', 'doc', 'docx'];
    const fileExtension = this.selectedFile.name.split('.').pop()?.toLowerCase();
    const maxSize = 5 * 1024 * 1024; // 5MB

    this.uploadError = null;

    if (!fileExtension || !allowedExtensions.includes(fileExtension)) {
      this.uploadError = 'Only PDF, DOC, and DOCX files are allowed';
      this.selectedFile = null;
      return;
    }

    if (this.selectedFile.size > maxSize) {
      this.uploadError = 'File size exceeds 5MB limit';
      this.selectedFile = null;
      return;
    }
  }

  uploadResume(): void {
    if (!this.selectedFile) {
      this.snackBar.open('Please select a file', 'Close', { duration: 3000 });
      return;
    }

    const userId = this.authService.getUserId();
    if (!userId) {
      this.snackBar.open('User ID not found', 'Close', { duration: 3000 });
      return;
    }

    this.isUploading = true;
    this.uploadSuccess = false;
    this.uploadError = null;

    this.jobSeekerService.uploadResume(userId, this.selectedFile).subscribe({
      next: (response: string) => {
        this.isUploading = false;
        this.uploadSuccess = true;
        this.snackBar.open('Resume uploaded successfully!', 'Close', { duration: 3000 });
        setTimeout(() => {
          this.clearSelection();
        }, 2000);
      },
      error: (error: any) => {
        this.isUploading = false;
        const errorMessage = error.error || 'Failed to upload resume';
        this.uploadError = errorMessage;
        this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
      }
    });
  }

  clearSelection(): void {
    this.selectedFile = null;
    this.uploadSuccess = false;
    this.uploadError = null;
  }
}
