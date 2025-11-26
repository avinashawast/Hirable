import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ErrorNotificationService {
  constructor(private snackBar: MatSnackBar) {}

  showError(message: string, duration: number = 5000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }

  showSuccess(message: string, duration: number = 3000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    });
  }

  showWarning(message: string, duration: number = 4000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['warning-snackbar']
    });
  }
}
