import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import { Stomp, CompatClient } from '@stomp/stompjs';

export interface ChatDTO {
  id?: number;
  recruiterId: number;
  jobSeekerId: number;
  recruiterName: string;
  jobSeekerName: string;
  createdAt?: string;
  flagged?: boolean;
  flagReason?: string;
}

export interface MessageDTO {
  id?: number;
  chatId: number;
  senderId: number;
  senderName: string;
  content: string;
  sentAt?: string;
  read?: boolean;
}

export interface ChatMessageRequest {
  chatId: number;
  recipientId: number;
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api';
  private stompClient: CompatClient | null = null;
  private messageSubject = new Subject<MessageDTO>();
  public messages$ = this.messageSubject.asObservable();

  constructor(private http: HttpClient) {
    this.connect();
  }

  private connect(): void {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);
    if (this.stompClient) {
      this.stompClient.connect({}, (frame: any) => {
        console.log('WebSocket connected:', frame);
      }, (error: any) => {
        console.error('WebSocket connection error:', error);
      });
    }
  }

  subscribeToMessages(userId: number): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.subscribe(`/user/${userId}/queue/messages`, (message: any) => {
        const messageDTO = JSON.parse(message.body);
        this.messageSubject.next(messageDTO);
      });
    }
  }

  sendMessage(message: ChatMessageRequest): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send('/app/chat.send', {}, JSON.stringify(message));
    }
  }

  getUserChats(userId: number): Observable<ChatDTO[]> {
    return this.http.get<ChatDTO[]>(`${this.apiUrl}/chats/${userId}`);
  }

  createChat(recruiterId: number, jobSeekerId: number): Observable<ChatDTO> {
    return this.http.post<ChatDTO>(`${this.apiUrl}/chats`, {
      recruiterId,
      jobSeekerId
    });
  }

  getChatMessages(chatId: number): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`${this.apiUrl}/chats/${chatId}/messages`);
  }

  getChatById(chatId: number): Observable<ChatDTO> {
    return this.http.get<ChatDTO>(`${this.apiUrl}/chats/${chatId}`);
  }

  getAllChats(): Observable<ChatDTO[]> {
    return this.http.get<ChatDTO[]>(`${this.apiUrl}/chats/all`);
  }

  flagChat(chatId: number, reason: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/chats/${chatId}/flag`, { reason });
  }

  disconnect(): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.disconnect(() => {
        console.log('WebSocket disconnected');
      });
    }
  }
}
