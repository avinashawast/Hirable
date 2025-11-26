import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, ChatDTO, MessageDTO, ChatMessageRequest } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  chats: ChatDTO[] = [];
  selectedChat: ChatDTO | null = null;
  messages: MessageDTO[] = [];
  messageContent: string = '';
  currentUserId: number = 0;
  loading: boolean = false;
  error: string = '';
  private destroy$ = new Subject<void>();

  constructor(
    private chatService: ChatService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    this.loadChats();
    this.subscribeToMessages();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.chatService.disconnect();
  }

  loadChats(): void {
    this.loading = true;
    this.chatService.getUserChats(this.currentUserId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (chats) => {
          this.chats = chats;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load chats';
          this.loading = false;
          console.error(err);
        }
      });
  }

  selectChat(chat: ChatDTO): void {
    this.selectedChat = chat;
    this.messages = [];
    this.loadMessages(chat.id!);
  }

  loadMessages(chatId: number): void {
    this.chatService.getChatMessages(chatId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (messages) => {
          this.messages = messages;
        },
        error: (err) => {
          this.error = 'Failed to load messages';
          console.error(err);
        }
      });
  }

  sendMessage(): void {
    if (!this.messageContent.trim() || !this.selectedChat) {
      return;
    }

    const recipientId = this.selectedChat.recruiterId === this.currentUserId
      ? this.selectedChat.jobSeekerId
      : this.selectedChat.recruiterId;

    const message: ChatMessageRequest = {
      chatId: this.selectedChat.id!,
      recipientId: recipientId,
      content: this.messageContent
    };

    this.chatService.sendMessage(message);
    this.messageContent = '';
  }

  private subscribeToMessages(): void {
    this.chatService.subscribeToMessages(this.currentUserId);
    this.chatService.messages$
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (message) => {
          if (this.selectedChat && message.chatId === this.selectedChat.id) {
            this.messages.push(message);
          }
        }
      });
  }

  getOtherPartyName(): string {
    if (!this.selectedChat) return '';
    return this.selectedChat.recruiterId === this.currentUserId
      ? this.selectedChat.jobSeekerName
      : this.selectedChat.recruiterName;
  }
}
