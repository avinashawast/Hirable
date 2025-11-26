import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-disable-user-chat-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    FormsModule
  ],
  template: `
    <h2 mat-dialog-title>Disable User Chat</h2>
    <mat-dialog-content>
      <div class="user-selection">
        <p>Select user to disable chat:</p>
        <mat-radio-group [(ngModel)]="selectedUserId">
          <mat-radio-button [value]="recruiterUserId" class="radio-option">
            {{ data.recruiterName }} (Recruiter)
          </mat-radio-button>
          <mat-radio-button [value]="jobSeekerUserId" class="radio-option">
            {{ data.jobSeekerName }} (Job Seeker)
          </mat-radio-button>
        </mat-radio-group>
      </div>
      <mat-form-field appearance="fill" class="full-width">
        <mat-label>Reason for disabling</mat-label>
        <textarea matInput [(ngModel)]="reason" rows="4" placeholder="Enter reason..."></textarea>
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="warn" (click)="onDisable()" [disabled]="!selectedUserId || !reason.trim()">
        Disable Chat
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .full-width {
      width: 100%;
      margin-top: 20px;
    }
    mat-dialog-content {
      padding: 20px 0;
    }
    .user-selection {
      margin-bottom: 20px;
    }
    .radio-option {
      display: block;
      margin-bottom: 10px;
    }
  `]
})
export class DisableUserChatDialogComponent {
  selectedUserId: number | null = null;
  reason = '';
  recruiterUserId: number;
  jobSeekerUserId: number;

  constructor(
    public dialogRef: MatDialogRef<DisableUserChatDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.recruiterUserId = data.recruiterId || 0;
    this.jobSeekerUserId = data.jobSeekerId || 0;
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onDisable(): void {
    if (this.selectedUserId && this.reason.trim()) {
      this.dialogRef.close({ userId: this.selectedUserId, reason: this.reason });
    }
  }
}
