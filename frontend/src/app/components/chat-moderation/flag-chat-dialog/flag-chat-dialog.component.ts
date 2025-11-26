import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-flag-chat-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule
  ],
  template: `
    <h2 mat-dialog-title>Flag Chat</h2>
    <mat-dialog-content>
      <mat-form-field appearance="fill" class="full-width">
        <mat-label>Reason for flagging</mat-label>
        <textarea matInput [(ngModel)]="reason" rows="4" placeholder="Enter reason..."></textarea>
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="warn" (click)="onFlag()" [disabled]="!reason.trim()">
        Flag Chat
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .full-width {
      width: 100%;
    }
    mat-dialog-content {
      padding: 20px 0;
    }
  `]
})
export class FlagChatDialogComponent {
  reason = '';

  constructor(
    public dialogRef: MatDialogRef<FlagChatDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onFlag(): void {
    if (this.reason.trim()) {
      this.dialogRef.close({ reason: this.reason });
    }
  }
}
