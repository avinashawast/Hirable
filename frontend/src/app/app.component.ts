import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { NavigationComponent } from './components/navigation/navigation.component';
import { AuthService } from './services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavigationComponent],
  template: `
    <div class="app-container">
      <app-navigation *ngIf="shouldShowNavigation()">
        <div class="router-outlet-wrapper page-transition-enter">
          <router-outlet></router-outlet>
        </div>
      </app-navigation>
      <div class="router-outlet-wrapper page-transition-enter" *ngIf="!shouldShowNavigation()">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
    }

    .router-outlet-wrapper {
      animation: fadeInSlideUp var(--animation-duration-slow) var(--animation-easing-ease-out);
    }
  `]
})
export class AppComponent implements OnInit {
  title = 'Hirable';
  isAuthenticated = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe(isAuth => {
      this.isAuthenticated = isAuth;
    });

    // Trigger page transition animation on route changes
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        // Animation is applied via CSS class
      });
  }

  shouldShowNavigation(): boolean {
    // Don't show navigation on login page
    if (this.router.url === '/login') {
      return false;
    }
    // Show navigation only if authenticated
    return this.isAuthenticated;
  }
}
