import { inject } from '@angular/core';
import {
  HttpRequest,
  HttpHandlerFn,
  HttpEvent
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

export function authInterceptor(request: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('authInterceptor: Processing request:', request.url);
  const token = authService.getToken();
  console.log('authInterceptor: Token from service:', token ? 'Present' : 'Missing');
  console.log('authInterceptor: Token value:', token);

  if (token) {
    console.log('authInterceptor: Adding JWT token to request:', request.url);
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('authInterceptor: Request headers after clone:', request.headers.get('Authorization'));
  } else {
    console.warn('authInterceptor: No JWT token found for request:', request.url);
  }

  return next(request).pipe(
    catchError((error: any) => {
      console.error('authInterceptor: HTTP Error:', error.status, error.message);
      if (error.status === 401) {
        // Only logout if not already on login page
        if (router.url !== '/login') {
          authService.logout();
          router.navigate(['/login']);
        }
      }
      return throwError(() => error);
    })
  );
}
