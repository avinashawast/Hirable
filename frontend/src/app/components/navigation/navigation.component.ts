import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../services/auth.service';
import { Observable } from 'rxjs';

interface NavItem {
  label: string;
  route: string;
  icon: string;
  roles: string[];
}

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatSidenavModule,
    MatBadgeModule,
    MatDividerModule
  ],
  template: `
    <header class="navbar">
      <button 
        class="menu-button"
        (click)="sidenav.toggle()"
        aria-label="Toggle navigation menu">
        <mat-icon>menu</mat-icon>
      </button>
      
      <span class="spacer"></span>
      <h1 class="app-title">Hirable</h1>
      <span class="spacer"></span>

      <button 
        class="user-menu-button"
        [matMenuTriggerFor]="userMenu"
        aria-label="User menu">
        <mat-icon>account_circle</mat-icon>
      </button>

      <mat-menu #userMenu="matMenu">
        <button mat-menu-item disabled>
          <span>{{ currentUsername }}</span>
        </button>
        <mat-divider></mat-divider>
        <button mat-menu-item (click)="navigateTo('/profile')">
          <mat-icon>person</mat-icon>
          <span>Profile</span>
        </button>
        <button mat-menu-item (click)="logout()">
          <mat-icon>logout</mat-icon>
          <span>Logout</span>
        </button>
      </mat-menu>
    </header>

    <mat-sidenav-container class="sidenav-container">
      <mat-sidenav 
        #sidenav 
        [mode]="(isDesktop$ | async) ? 'side' : 'over'"
        [opened]="(isDesktop$ | async) ? true : false"
        class="sidenav">
        <nav class="nav-list" role="navigation" aria-label="Main navigation">
          <a 
            *ngFor="let item of navItems"
            [routerLink]="item.route"
            routerLinkActive="active"
            class="nav-item"
            (click)="closeSidenavOnMobile()">
            <mat-icon class="nav-icon" aria-hidden="true">{{ item.icon }}</mat-icon>
            <span class="nav-label">{{ item.label }}</span>
          </a>
        </nav>
      </mat-sidenav>

      <mat-sidenav-content class="content">
        <ng-content></ng-content>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styles: [`
    .navbar {
      display: flex;
      align-items: center;
      position: sticky;
      top: 0;
      z-index: 100;
      height: 64px;
      background-color: var(--color-primary);
      color: white;
      box-shadow: var(--shadow-sm);
      padding: 0 var(--spacing-md);
    }

    .menu-button {
      display: none;
      background: none;
      border: none;
      color: white;
      cursor: pointer;
      padding: var(--spacing-sm);
      margin-right: var(--spacing-md);
      border-radius: var(--border-radius-md);
      transition: background-color var(--animation-duration-standard) var(--animation-easing-ease-in-out);
    }

    .menu-button:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }

    .menu-button:focus {
      outline: 2px solid white;
      outline-offset: 2px;
    }

    .app-title {
      font-size: var(--font-size-title-large);
      font-weight: var(--font-weight-semibold);
      margin: 0;
      white-space: nowrap;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .user-menu-button {
      background: none;
      border: none;
      color: white;
      cursor: pointer;
      padding: var(--spacing-sm);
      margin-left: var(--spacing-md);
      border-radius: var(--border-radius-md);
      display: flex;
      align-items: center;
      justify-content: center;
      transition: background-color var(--animation-duration-standard) var(--animation-easing-ease-in-out);
    }

    .user-menu-button:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }

    .user-menu-button:focus {
      outline: 2px solid white;
      outline-offset: 2px;
    }

    .sidenav-container {
      height: calc(100vh - 64px);
    }

    .sidenav {
      width: 280px;
      background-color: var(--color-neutral-50);
      border-right: 1px solid var(--color-neutral-200);
    }

    .content {
      padding: var(--spacing-lg);
      overflow-y: auto;
      background-color: white;
    }

    .nav-list {
      padding: var(--spacing-md) 0;
      list-style: none;
      display: flex;
      flex-direction: column;
      margin: 0;
    }

    .nav-item {
      display: flex;
      align-items: center;
      padding: 12px var(--spacing-md);
      margin: 0 var(--spacing-sm);
      cursor: pointer;
      border-left: 4px solid transparent;
      border-radius: var(--border-radius-md);
      transition: all var(--animation-duration-standard) var(--animation-easing-ease-in-out);
      text-decoration: none;
      color: var(--color-neutral-700);
      font-size: var(--font-size-body-medium);
      font-weight: var(--font-weight-medium);
    }

    .nav-item:hover {
      background-color: var(--color-neutral-100);
      color: var(--color-primary);
    }

    .nav-item.active {
      background-color: rgba(25, 118, 210, 0.08);
      border-left-color: var(--color-primary);
      color: var(--color-primary);
    }

    .nav-icon {
      margin-right: var(--spacing-md);
      font-size: 24px;
      width: 24px;
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .nav-label {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      flex: 1;
    }

    @media (max-width: 599px) {
      .navbar {
        height: 56px;
        padding: 0 var(--spacing-sm);
      }

      .menu-button {
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .app-title {
        font-size: var(--font-size-title-medium);
      }

      .sidenav-container {
        height: calc(100vh - 56px);
      }

      .sidenav {
        width: 256px;
      }

      .content {
        padding: var(--spacing-md);
      }
    }

    @media (max-width: 959px) {
      .menu-button {
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  `]
})
export class NavigationComponent implements OnInit {
  navItems: NavItem[] = [];
  currentUsername: string | null = null;
  isDesktop$: Observable<boolean>;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.isDesktop$ = new Observable((observer: any) => {
      const checkWidth = () => {
        // Desktop breakpoint is 960px according to design system
        observer.next(window.innerWidth >= 960);
      };
      checkWidth();
      window.addEventListener('resize', checkWidth);
      return () => window.removeEventListener('resize', checkWidth);
    });
  }

  ngOnInit(): void {
    this.currentUsername = this.authService.getUsername();
    this.buildNavigation();
  }

  private buildNavigation(): void {
    const userRole = this.authService.getRole();

    const allItems: NavItem[] = [
      // Common items
      { label: 'Dashboard', route: '/dashboard', icon: 'dashboard', roles: ['ADMIN', 'JOB_SEEKER', 'RECRUITER'] },

      // Job Seeker items
      { label: 'Search Jobs', route: '/job-search', icon: 'search', roles: ['JOB_SEEKER'] },
      { label: 'My Applications', route: '/job-seeker-dashboard', icon: 'assignment', roles: ['JOB_SEEKER'] },
      { label: 'Upload Resume', route: '/resume-upload', icon: 'upload_file', roles: ['JOB_SEEKER'] },
      { label: 'My Profile', route: '/profile', icon: 'person', roles: ['JOB_SEEKER'] },

      // Recruiter items
      { label: 'Post Job', route: '/job-posting-form', icon: 'add_circle', roles: ['RECRUITER'] },
      { label: 'My Jobs', route: '/recruiter-dashboard', icon: 'work', roles: ['RECRUITER'] },
      { label: 'Search Candidates', route: '/candidate-search', icon: 'people', roles: ['RECRUITER'] },
      { label: 'My Shortlist', route: '/shortlist', icon: 'favorite', roles: ['RECRUITER'] },
      { label: 'Company Profile', route: '/company-profile', icon: 'business', roles: ['RECRUITER'] },

      // Admin items
      { label: 'Admin Dashboard', route: '/admin-dashboard', icon: 'admin_panel_settings', roles: ['ADMIN'] },
      { label: 'User Management', route: '/user-management', icon: 'manage_accounts', roles: ['ADMIN'] },
      { label: 'Job Management', route: '/job-management', icon: 'assignment_turned_in', roles: ['ADMIN'] },
      { label: 'Talent Pool', route: '/talent-pool', icon: 'group', roles: ['ADMIN'] },
      { label: 'Chat Moderation', route: '/chat-moderation', icon: 'chat', roles: ['ADMIN'] },
      { label: 'System Config', route: '/system-config', icon: 'settings', roles: ['ADMIN'] },
      { label: 'Analytics', route: '/analytics', icon: 'analytics', roles: ['ADMIN'] },

      // Common items
      { label: 'Messages', route: '/chat', icon: 'message', roles: ['JOB_SEEKER', 'RECRUITER'] },
      { label: 'Notifications', route: '/notification', icon: 'notifications', roles: ['ADMIN', 'JOB_SEEKER', 'RECRUITER'] }
    ];

    this.navItems = allItems.filter(item => 
      userRole && item.roles.includes(userRole)
    );
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  closeSidenavOnMobile(): void {
    if (window.innerWidth < 960) {
      const sidenav = document.querySelector('mat-sidenav');
      if (sidenav) {
        (sidenav as any).close();
      }
    }
  }
}
