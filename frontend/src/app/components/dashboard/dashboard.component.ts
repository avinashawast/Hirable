import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule
  ],
  template: `
    <mat-toolbar color="primary">
      <span>Hirable</span>
      <span class="spacer"></span>
      <button mat-button [matMenuTriggerFor]="menu">
        {{ username }}
        <mat-icon>arrow_drop_down</mat-icon>
      </button>
      <mat-menu #menu="matMenu">
        <button mat-menu-item (click)="navigateTo('/profile')">Profile</button>
        <button mat-menu-item (click)="navigateTo('/chat')">Chat</button>
        <button mat-menu-item *ngIf="isAdmin()" (click)="navigateTo('/user-management')">User Management</button>
        <button mat-menu-item *ngIf="isAdmin()" (click)="navigateTo('/job-management')">Job Management</button>
        <button mat-menu-item (click)="logout()">Logout</button>
      </mat-menu>
    </mat-toolbar>
    <div class="dashboard-content">
      <div class="role-based-content">
        <div *ngIf="isAdmin()" class="admin-dashboard">
          <h2>Admin Dashboard</h2>
          <div class="button-group">
            <button mat-raised-button color="primary" (click)="navigateTo('/admin-dashboard')">
              <mat-icon>dashboard</mat-icon> Dashboard
            </button>
            <button mat-raised-button color="primary" (click)="navigateTo('/analytics')">
              <mat-icon>analytics</mat-icon> Analytics
            </button>
            <button mat-raised-button color="primary" (click)="navigateTo('/user-management')">
              <mat-icon>people</mat-icon> User Management
            </button>
            <button mat-raised-button color="primary" (click)="navigateTo('/job-management')">
              <mat-icon>work</mat-icon> Job Management
            </button>
          </div>
        </div>
        <div *ngIf="isJobSeeker()" class="job-seeker-dashboard">
          <h2>Job Seeker Dashboard</h2>
          <div class="button-group">
            <button mat-raised-button color="primary" (click)="navigateTo('/job-search')">
              <mat-icon>search</mat-icon> Search Jobs
            </button>
            <button mat-raised-button color="primary" (click)="navigateTo('/job-seeker-dashboard')">
              <mat-icon>dashboard</mat-icon> My Applications
            </button>
          </div>
        </div>
        <div *ngIf="isRecruiter()" class="recruiter-dashboard">
          <h2>Recruiter Dashboard</h2>
          <div class="button-group">
            <button mat-raised-button color="primary" (click)="navigateTo('/recruiter-dashboard')">
              <mat-icon>dashboard</mat-icon> My Jobs
            </button>
            <button mat-raised-button color="primary" (click)="navigateTo('/candidate-search')">
              <mat-icon>search</mat-icon> Search Candidates
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    mat-toolbar {
      margin-bottom: 20px;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .dashboard-content {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .role-based-content {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 400px;
    }

    .admin-dashboard,
    .job-seeker-dashboard,
    .recruiter-dashboard {
      text-align: center;
    }

    h2 {
      margin-bottom: 30px;
      color: #333;
    }

    .button-group {
      display: flex;
      gap: 15px;
      justify-content: center;
      flex-wrap: wrap;
    }

    button {
      min-width: 150px;
    }

    mat-icon {
      margin-right: 8px;
    }
  `]
})
export class DashboardComponent implements OnInit {
  username: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
    }
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ADMIN');
  }

  isJobSeeker(): boolean {
    return this.authService.hasRole('JOB_SEEKER');
  }

  isRecruiter(): boolean {
    return this.authService.hasRole('RECRUITER');
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
