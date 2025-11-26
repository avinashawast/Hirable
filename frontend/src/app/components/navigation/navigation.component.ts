import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatBadgeModule } from '@angular/material/badge';
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
    MatListModule,
    MatBadgeModule
  ],
  template: `
    <mat-toolbar color="primary" class="navbar">
      <button 
        mat-icon-button 
        (click)="sidenav.toggle()"
        class="menu-button">
        <mat-icon>menu</mat-icon>
      </button>
      
      <span class="spacer"></span>
      <span class="app-title">Hirable</span>
      <span class="spacer"></span>

      <button 
        mat-icon-button 
        [matMenuTriggerFor]="userMenu"
        class="user-menu-button">
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
    </mat-toolbar>

    <mat-sidenav-container class="sidenav-container">
      <mat-sidenav 
        #sidenav 
        mode="side" 
        [opened]="isDesktop$ | async"
        class="sidenav">
        <mat-nav-list>
          <mat-list-item 
            *ngFor="let item of navItems"
            [routerLink]="item.route"
            routerLinkActive="active"
            (click)="closeSidenavOnMobile()">
            <mat-icon matListItemIcon>{{ item.icon }}</mat-icon>
            <span matListItemTitle>{{ item.label }}</span>
          </mat-list-item>
        </mat-nav-list>
      </mat-sidenav>

      <mat-sidenav-content class="content">
        <ng-content></ng-content>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styles: [`
    .navbar {
      position: sticky;
      top: 0;
      z-index: 100;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .menu-button {
      margin-right: 16px;
    }

    .app-title {
      font-size: 20px;
      font-weight: 500;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .user-menu-button {
      margin-left: 16px;
    }

    .sidenav-container {
      height: calc(100vh - 64px);
    }

    .sidenav {
      width: 250px;
      border-right: 1px solid #e0e0e0;
    }

    .content {
      padding: 20px;
      overflow-y: auto;
    }

    mat-nav-list {
      padding-top: 0;
    }

    mat-list-item {
      cursor: pointer;
      border-left: 4px solid transparent;
      transition: all 0.3s ease;
    }

    mat-list-item.active {
      background-color: rgba(63, 81, 181, 0.08);
      border-left-color: #3f51b5;
    }

    mat-list-item:hover {
      background-color: rgba(0, 0, 0, 0.04);
    }

    @media (max-width: 768px) {
      .sidenav-container {
        height: calc(100vh - 56px);
      }

      .navbar {
        height: 56px;
      }

      .app-title {
        font-size: 18px;
      }

      .content {
        padding: 16px;
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
        observer.next(window.innerWidth > 768);
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
    if (window.innerWidth <= 768) {
      const sidenav = document.querySelector('mat-sidenav');
      if (sidenav) {
        (sidenav as any).close();
      }
    }
  }
}
