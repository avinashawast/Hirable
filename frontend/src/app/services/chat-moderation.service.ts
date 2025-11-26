import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatDTO {
  id: number;
  recruiterId: number;
  jobSeekerId: number;
  recruiterName: string;
  jobSeekerName: string;
  createdAt: string;
  flagged: boolean;
  flagReason: string;
  messageCount: number;
}

export interface MessageDTO {
  id: number;
  chatId: number;
  senderId: number;
  senderName: string;
  content: string;
  sentAt: string;
  read: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ChatModerationService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAllChats(): Observable<ChatDTO[]> {
    return this.http.get<ChatDTO[]>(`${this.apiUrl}/chats/all`);
  }

  flagChat(chatId: number, reason: string, adminId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/chats/${chatId}/flag`, {
      reason,
      adminId
    });
  }

  unflagChat(chatId: number, adminId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/chats/${chatId}/unflag`, {
      adminId
    });
  }

  disableUserChat(userId: number, adminId: number, reason: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/users/${userId}/disable-chat`, {
      adminId,
      reason
    });
  }

  enableUserChat(userId: number, adminId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/users/${userId}/enable-chat`, {
      adminId
    });
  }

  getChatMessages(chatId: number): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`http://localhost:8080/api/chats/${chatId}/messages`);
  }
}
