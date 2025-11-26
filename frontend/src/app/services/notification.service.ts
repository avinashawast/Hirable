import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NotificationDTO {
  id?: number;
  userId: number;
  type: string;
  message: string;
  read: boolean;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getUserNotifications(userId: number): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiUrl}/notifications/${userId}`);
  }

  markAsRead(notificationId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/notifications/${notificationId}/read`, {});
  }

  getUnreadCount(userId: number): Observable<{ unreadCount: number }> {
    return this.http.get<{ unreadCount: number }>(`${this.apiUrl}/notifications/${userId}/unread-count`);
  }
}
