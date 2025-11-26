import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  template: `
    <div class="spinner-container" *ngIf="isLoading">
      <mat-spinner [diameter]="diameter"></mat-spinner>
      <p *ngIf="message" class="spinner-message">{{ message }}</p>
    </div>
  `,
  styles: [`
    .spinner-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px;
      gap: 16px;
    }

    .spinner-message {
      margin: 0;
      color: #666;
      font-size: 14px;
    }
  `]
})
export class LoadingSpinnerComponent {
  @Input() isLoading = false;
  @Input() message: string | null = null;
  @Input() diameter = 50;
}
