import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { AnalyticsService, AnalyticsDTO } from '../../services/analytics.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatGridListModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  analytics: AnalyticsDTO | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private analyticsService: AnalyticsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAnalytics();
  }

  loadAnalytics(): void {
    this.loading = true;
    this.error = null;
    this.analyticsService.getAnalytics().subscribe({
      next: (data) => {
        this.analytics = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load analytics data';
        this.loading = false;
        console.error('Error loading analytics:', err);
      }
    });
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  viewFullAnalytics(): void {
    this.router.navigate(['/analytics']);
  }
}
