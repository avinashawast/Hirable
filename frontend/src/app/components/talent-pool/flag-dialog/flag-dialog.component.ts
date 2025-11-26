import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TalentPoolProfileDTO } from '../../../services/talent-pool.service';

@Component({
  selector: 'app-flag-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './flag-dialog.component.html',
  styleUrls: ['./flag-dialog.component.css']
})
export class FlagDialogComponent {
  reason = '';

  constructor(
    public dialogRef: MatDialogRef<FlagDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { profile: TalentPoolProfileDTO }
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onFlag(): void {
    if (this.reason.trim()) {
      this.dialogRef.close(this.reason);
    }
  }
}
