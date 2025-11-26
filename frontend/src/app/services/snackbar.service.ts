import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  constructor(private snackBar: MatSnackBar) {}

  success(message: string, duration = 3000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-success']
    });
  }

  error(message: string, duration = 5000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-error']
    });
  }

  info(message: string, duration = 3000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-info']
    });
  }

  warning(message: string, duration = 4000): void {
    this.snackBar.open(message, 'Close', {
      duration: duration,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-warning']
    });
  }
}
