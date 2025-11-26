import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent, ConfirmationDialogData } from '../components/shared/confirmation-dialog/confirmation-dialog.component';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  confirm(data: ConfirmationDialogData): Observable<boolean> {
    return this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: data,
      disableClose: false
    }).afterClosed();
  }

  confirmDelete(itemName: string): Observable<boolean> {
    return this.confirm({
      title: 'Confirm Delete',
      message: `Are you sure you want to delete ${itemName}? This action cannot be undone.`,
      confirmText: 'Delete',
      cancelText: 'Cancel',
      isDangerous: true
    });
  }

  confirmAction(title: string, message: string): Observable<boolean> {
    return this.confirm({
      title: title,
      message: message,
      confirmText: 'Confirm',
      cancelText: 'Cancel'
    });
  }
}
