import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ErrorNotificationService } from '../services/error-notification.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private errorNotificationService: ErrorNotificationService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unexpected error occurred';

        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = error.error.message;
        } else {
          // Server-side error
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else if (error.statusText) {
            errorMessage = error.statusText;
          }

          // Handle specific status codes
          switch (error.status) {
            case 400:
              errorMessage = error.error?.message || 'Bad request. Please check your input.';
              break;
            case 401:
              errorMessage = 'Your session has expired. Please log in again.';
              this.router.navigate(['/login']);
              break;
            case 403:
              errorMessage = 'You do not have permission to perform this action.';
              break;
            case 404:
              errorMessage = error.error?.message || 'The requested resource was not found.';
              break;
            case 500:
              errorMessage = 'Server error. Please try again later.';
              break;
            case 503:
              errorMessage = 'Service unavailable. Please try again later.';
              break;
          }
        }

        this.errorNotificationService.showError(errorMessage);
        return throwError(() => error);
      })
    );
  }
}
