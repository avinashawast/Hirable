import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-error-display',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <mat-card class="error-card" *ngIf="error">
      <mat-card-content>
        <div class="error-content">
          <mat-icon class="error-icon">error_outline</mat-icon>
          <div class="error-text">
            <h3>{{ title }}</h3>
            <p>{{ error }}</p>
          </div>
          <button 
            mat-icon-button 
            (click)="onDismiss()"
            class="close-button">
            <mat-icon>close</mat-icon>
          </button>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .error-card {
      background-color: #ffebee;
      border-left: 4px solid #f44336;
      margin-bottom: 16px;
    }

    mat-card-content {
      padding: 16px;
    }

    .error-content {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }

    .error-icon {
      color: #f44336;
      flex-shrink: 0;
      margin-top: 2px;
    }

    .error-text {
      flex: 1;
    }

    .error-text h3 {
      margin: 0 0 8px 0;
      color: #c62828;
      font-size: 16px;
    }

    .error-text p {
      margin: 0;
      color: #d32f2f;
      font-size: 14px;
    }

    .close-button {
      flex-shrink: 0;
    }
  `]
})
export class ErrorDisplayComponent {
  @Input() error: string | null = null;
  @Input() title = 'Error';
  @Output() dismissed = new EventEmitter<void>();

  onDismiss(): void {
    this.error = null;
    this.dismissed.emit();
  }
}
