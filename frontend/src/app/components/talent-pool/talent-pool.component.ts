import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { TalentPoolService, TalentPoolProfileDTO } from '../../services/talent-pool.service';
import { FlagDialogComponent } from './flag-dialog/flag-dialog.component';

@Component({
  selector: 'app-talent-pool',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatIconModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule,
    MatSelectModule
  ],
  templateUrl: './talent-pool.component.html',
  styleUrls: ['./talent-pool.component.css']
})
export class TalentPoolComponent implements OnInit {
  profiles: TalentPoolProfileDTO[] = [];
  filteredProfiles: TalentPoolProfileDTO[] = [];
  searchQuery = '';
  flagFilter = '';
  showProfileDetails = false;
  selectedProfile: TalentPoolProfileDTO | null = null;

  displayedColumns: string[] = ['name', 'email', 'location', 'skills', 'flagged', 'actions'];

  constructor(
    private talentPoolService: TalentPoolService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadProfiles();
  }

  loadProfiles(): void {
    this.talentPoolService.getAllProfiles().subscribe({
      next: (data: TalentPoolProfileDTO[]) => {
        this.profiles = data;
        this.applyFilters();
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load profiles', 'Close', { duration: 3000 });
        console.error('Error loading profiles:', error);
      }
    });
  }

  applyFilters(): void {
    this.filteredProfiles = this.profiles.filter(profile => {
      const matchesSearch = 
        `${profile.firstName} ${profile.lastName}`.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        profile.email.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        profile.location.toLowerCase().includes(this.searchQuery.toLowerCase());
      
      const matchesFlag = !this.flagFilter || 
                         (this.flagFilter === 'flagged' ? profile.flagged : !profile.flagged);
      
      return matchesSearch && matchesFlag;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onFlagFilterChange(): void {
    this.applyFilters();
  }

  viewProfile(profile: TalentPoolProfileDTO): void {
    this.selectedProfile = profile;
    this.showProfileDetails = true;
  }

  closeProfileDetails(): void {
    this.showProfileDetails = false;
    this.selectedProfile = null;
  }

  downloadResume(profile: TalentPoolProfileDTO): void {
    if (profile.resumeFilePath) {
      const link = document.createElement('a');
      link.href = `http://localhost:8080/api/jobseekers/${profile.id}/resume/download`;
      link.download = `${profile.firstName}_${profile.lastName}_resume`;
      link.click();
    }
  }

  openFlagDialog(profile: TalentPoolProfileDTO): void {
    const dialogRef = this.dialog.open(FlagDialogComponent, {
      width: '400px',
      data: { profile }
    });

    dialogRef.afterClosed().subscribe((result: string | undefined) => {
      if (result) {
        this.talentPoolService.flagProfile(profile.id, result).subscribe({
          next: () => {
            this.snackBar.open('Profile flagged successfully', 'Close', { duration: 3000 });
            this.loadProfiles();
          },
          error: (error: any) => {
            this.snackBar.open('Failed to flag profile', 'Close', { duration: 3000 });
            console.error('Error flagging profile:', error);
          }
        });
      }
    });
  }

  unflagProfile(profile: TalentPoolProfileDTO): void {
    if (confirm('Are you sure you want to unflag this profile?')) {
      this.talentPoolService.unflagProfile(profile.id).subscribe({
        next: () => {
          this.snackBar.open('Profile unflagged successfully', 'Close', { duration: 3000 });
          this.loadProfiles();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to unflag profile', 'Close', { duration: 3000 });
          console.error('Error unflagging profile:', error);
        }
      });
    }
  }

  removeProfile(profile: TalentPoolProfileDTO): void {
    if (confirm(`Are you sure you want to remove ${profile.firstName} ${profile.lastName}'s profile? This action cannot be undone.`)) {
      this.talentPoolService.removeProfile(profile.id).subscribe({
        next: () => {
          this.snackBar.open('Profile removed successfully', 'Close', { duration: 3000 });
          this.loadProfiles();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to remove profile', 'Close', { duration: 3000 });
          console.error('Error removing profile:', error);
        }
      });
    }
  }

  getSkillsDisplay(skills: string[]): string {
    return skills.slice(0, 3).join(', ') + (skills.length > 3 ? '...' : '');
  }
}
