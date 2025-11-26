import { Component, OnInit, OnDestroy } from '@angular/core';
import { NotificationService, NotificationDTO } from '../../services/notification.service';
import { AuthService } from '../../services/auth.service';
import { Subject, interval } from 'rxjs';
import { takeUntil, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {
  notifications: NotificationDTO[] = [];
  unreadCount: number = 0;
  showNotifications: boolean = false;
  userId: number | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId();
    if (this.userId) {
      this.loadNotifications();
      // Auto-refresh notifications every 30 seconds
      interval(30000)
        .pipe(
          takeUntil(this.destroy$),
          switchMap(() => this.notificationService.getUserNotifications(this.userId!))
        )
        .subscribe(notifications => {
          this.notifications = notifications;
          this.updateUnreadCount();
        });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadNotifications(): void {
    if (this.userId) {
      this.notificationService.getUserNotifications(this.userId).subscribe(
        notifications => {
          this.notifications = notifications;
          this.updateUnreadCount();
        },
        error => console.error('Error loading notifications:', error)
      );
    }
  }

  updateUnreadCount(): void {
    if (this.userId) {
      this.notificationService.getUnreadCount(this.userId).subscribe(
        response => {
          this.unreadCount = response.unreadCount;
        },
        error => console.error('Error loading unread count:', error)
      );
    }
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  markAsRead(notification: NotificationDTO): void {
    if (notification.id) {
      this.notificationService.markAsRead(notification.id).subscribe(
        () => {
          notification.read = true;
          this.updateUnreadCount();
        },
        error => console.error('Error marking notification as read:', error)
      );
    }
  }

  getNotificationIcon(type: string): string {
    switch (type) {
      case 'JOB_APPLICATION':
        return 'assignment';
      case 'RECRUITER_INTEREST':
        return 'favorite';
      case 'CHAT_MESSAGE':
        return 'message';
      case 'JOB_APPROVED':
        return 'check_circle';
      default:
        return 'notifications';
    }
  }
}
