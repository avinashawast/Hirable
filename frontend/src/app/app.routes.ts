import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'job-search',
    loadComponent: () => import('./components/job-search/job-search.component').then(m => m.JobSearchComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['JOB_SEEKER'] }
  },
  {
    path: 'job-details/:id',
    loadComponent: () => import('./components/job-details/job-details.component').then(m => m.JobDetailsComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'job-seeker-dashboard',
    loadComponent: () => import('./components/job-seeker-dashboard/job-seeker-dashboard.component').then(m => m.JobSeekerDashboardComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['JOB_SEEKER'] }
  },
  {
    path: 'candidate-search',
    loadComponent: () => import('./components/candidate-search/candidate-search.component').then(m => m.CandidateSearchComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'candidate-profile/:id',
    loadComponent: () => import('./components/candidate-profile/candidate-profile.component').then(m => m.CandidateProfileComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'chat',
    loadComponent: () => import('./components/chat/chat.component').then(m => m.ChatComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'user-management',
    loadComponent: () => import('./components/user-management/user-management.component').then(m => m.UserManagementComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'job-management',
    loadComponent: () => import('./components/job-management/job-management.component').then(m => m.JobManagementComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'admin-dashboard',
    loadComponent: () => import('./components/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'analytics',
    loadComponent: () => import('./components/analytics/analytics.component').then(m => m.AnalyticsComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'talent-pool',
    loadComponent: () => import('./components/talent-pool/talent-pool.component').then(m => m.TalentPoolComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'system-config',
    loadComponent: () => import('./components/system-config/system-config.component').then(m => m.SystemConfigComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'profile',
    loadComponent: () => import('./components/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'recruiter-dashboard',
    loadComponent: () => import('./components/recruiter-dashboard/recruiter-dashboard.component').then(m => m.RecruiterDashboardComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'company-profile',
    loadComponent: () => import('./components/company-profile/company-profile.component').then(m => m.CompanyProfileComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'job-posting-form',
    loadComponent: () => import('./components/job-posting-form/job-posting-form.component').then(m => m.JobPostingFormComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'shortlist',
    loadComponent: () => import('./components/shortlist/shortlist.component').then(m => m.ShortlistComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['RECRUITER'] }
  },
  {
    path: 'resume-upload',
    loadComponent: () => import('./components/resume-upload/resume-upload.component').then(m => m.ResumeUploadComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['JOB_SEEKER'] }
  },
  {
    path: 'notification',
    loadComponent: () => import('./components/notification/notification.component').then(m => m.NotificationComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'chat-moderation',
    loadComponent: () => import('./components/chat-moderation/chat-moderation.component').then(m => m.ChatModerationComponent),
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMIN'] }
  }
];
