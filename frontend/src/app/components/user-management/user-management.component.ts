import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UserService, UserDTO, CreateUserRequest } from '../../services/user.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatIconModule,
    MatTooltipModule,
    MatSnackBarModule
  ],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: UserDTO[] = [];
  filteredUsers: UserDTO[] = [];
  showCreateForm = false;
  showEditForm = false;
  selectedUser: UserDTO | null = null;
  searchQuery = '';
  roleFilter = '';
  statusFilter = '';

  newUser: CreateUserRequest = {
    username: '',
    email: '',
    password: '',
    role: 'JOB_SEEKER'
  };

  roles = ['ADMIN', 'JOB_SEEKER', 'RECRUITER'];

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.applyFilters();
      },
      error: (error) => {
        this.snackBar.open('Failed to load users', 'Close', { duration: 3000 });
        console.error('Error loading users:', error);
      }
    });
  }

  applyFilters(): void {
    this.filteredUsers = this.users.filter(user => {
      const matchesSearch = user.username.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                           user.email.toLowerCase().includes(this.searchQuery.toLowerCase());
      const matchesRole = !this.roleFilter || user.role === this.roleFilter;
      const matchesStatus = !this.statusFilter || 
                           (this.statusFilter === 'active' ? user.active : !user.active);
      return matchesSearch && matchesRole && matchesStatus;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onRoleFilterChange(): void {
    this.applyFilters();
  }

  onStatusFilterChange(): void {
    this.applyFilters();
  }

  openCreateForm(): void {
    this.showCreateForm = true;
    this.newUser = {
      username: '',
      email: '',
      password: '',
      role: 'JOB_SEEKER'
    };
  }

  closeCreateForm(): void {
    this.showCreateForm = false;
  }

  createUser(): void {
    if (!this.newUser.username || !this.newUser.email || !this.newUser.password) {
      this.snackBar.open('Please fill in all fields', 'Close', { duration: 3000 });
      return;
    }

    this.userService.createUser(this.newUser).subscribe({
      next: () => {
        this.snackBar.open('User created successfully', 'Close', { duration: 3000 });
        this.closeCreateForm();
        this.loadUsers();
      },
      error: (error) => {
        this.snackBar.open(error.error?.message || 'Failed to create user', 'Close', { duration: 3000 });
        console.error('Error creating user:', error);
      }
    });
  }

  openEditForm(user: UserDTO): void {
    this.selectedUser = { ...user };
    this.showEditForm = true;
  }

  closeEditForm(): void {
    this.showEditForm = false;
    this.selectedUser = null;
  }

  updateUser(): void {
    if (!this.selectedUser) return;

    this.userService.updateUser(this.selectedUser.id, this.selectedUser).subscribe({
      next: () => {
        this.snackBar.open('User updated successfully', 'Close', { duration: 3000 });
        this.closeEditForm();
        this.loadUsers();
      },
      error: (error) => {
        this.snackBar.open('Failed to update user', 'Close', { duration: 3000 });
        console.error('Error updating user:', error);
      }
    });
  }

  deactivateUser(user: UserDTO): void {
    if (confirm(`Are you sure you want to deactivate ${user.username}?`)) {
      this.userService.deactivateUser(user.id).subscribe({
        next: () => {
          this.snackBar.open('User deactivated successfully', 'Close', { duration: 3000 });
          this.loadUsers();
        },
        error: (error) => {
          this.snackBar.open('Failed to deactivate user', 'Close', { duration: 3000 });
          console.error('Error deactivating user:', error);
        }
      });
    }
  }

  activateUser(user: UserDTO): void {
    this.userService.activateUser(user.id).subscribe({
      next: () => {
        this.snackBar.open('User activated successfully', 'Close', { duration: 3000 });
        this.loadUsers();
      },
      error: (error) => {
        this.snackBar.open('Failed to activate user', 'Close', { duration: 3000 });
        console.error('Error activating user:', error);
      }
    });
  }
}
