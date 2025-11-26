import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { NavigationComponent } from './components/navigation/navigation.component';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavigationComponent],
  template: `
    <div class="app-container">
      <app-navigation *ngIf="isAuthenticated">
        <router-outlet></router-outlet>
      </app-navigation>
      <router-outlet *ngIf="!isAuthenticated"></router-outlet>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
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
  }
}
