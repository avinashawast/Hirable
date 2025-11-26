import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: string;
  userId: number;
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'auth_token';
  private roleKey = 'user_role';
  private userIdKey = 'user_id';
  private usernameKey = 'username';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: LoginResponse) => {
        this.storeToken(response.token);
        this.storeRole(response.role);
        this.storeUserId(response.userId);
        this.storeUsername(response.username);
        this.isAuthenticatedSubject.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.roleKey);
    localStorage.removeItem(this.userIdKey);
    localStorage.removeItem(this.usernameKey);
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): string | null {
    return localStorage.getItem(this.roleKey);
  }

  getUserId(): number | null {
    const userId = localStorage.getItem(this.userIdKey);
    return userId ? parseInt(userId, 10) : null;
  }

  getCurrentUserId(): number {
    const userId = this.getUserId();
    if (!userId) {
      throw new Error('User ID not found');
    }
    return userId;
  }

  getUsername(): string | null {
    return localStorage.getItem(this.usernameKey);
  }

  isAuthenticated(): boolean {
    return this.hasToken();
  }

  hasRole(role: string): boolean {
    return this.getRole() === role;
  }

  hasAnyRole(...roles: string[]): boolean {
    const userRole = this.getRole();
    return userRole ? roles.includes(userRole) : false;
  }

  private storeToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private storeRole(role: string): void {
    localStorage.setItem(this.roleKey, role);
  }

  private storeUserId(userId: number): void {
    localStorage.setItem(this.userIdKey, userId.toString());
  }

  private storeUsername(username: string): void {
    localStorage.setItem(this.usernameKey, username);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }
}
