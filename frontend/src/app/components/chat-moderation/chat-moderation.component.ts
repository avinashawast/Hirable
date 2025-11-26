import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { ChatModerationService } from '../../services/chat-moderation.service';
import { AuthService } from '../../services/auth.service';
import { FlagChatDialogComponent } from './flag-chat-dialog/flag-chat-dialog.component';
import { DisableUserChatDialogComponent } from './disable-user-chat-dialog/disable-user-chat-dialog.component';

interface ChatData {
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

@Component({
  selector: 'app-chat-moderation',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    FormsModule
  ],
  templateUrl: './chat-moderation.component.html',
  styleUrls: ['./chat-moderation.component.css']
})
export class ChatModerationComponent implements OnInit {
  chats: ChatData[] = [];
  loading = true;
  error: string | null = null;
  displayedColumns: string[] = ['recruiterName', 'jobSeekerName', 'messageCount', 'flagged', 'createdAt', 'actions'];
  currentUserId: number | null = null;

  constructor(
    private chatModerationService: ChatModerationService,
    private authService: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    this.loadChats();
  }

  loadChats(): void {
    this.loading = true;
    this.error = null;
    this.chatModerationService.getAllChats().subscribe({
      next: (data) => {
        this.chats = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load chats';
        this.loading = false;
        console.error('Error loading chats:', err);
      }
    });
  }

  flagChat(chat: ChatData): void {
    const dialogRef = this.dialog.open(FlagChatDialogComponent, {
      width: '400px',
      data: { chatId: chat.id }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.currentUserId) {
        this.chatModerationService.flagChat(chat.id, result.reason, this.currentUserId).subscribe({
          next: () => {
            this.snackBar.open('Chat flagged successfully', 'Close', { duration: 3000 });
            this.loadChats();
          },
          error: (err) => {
            this.snackBar.open('Failed to flag chat', 'Close', { duration: 3000 });
            console.error('Error flagging chat:', err);
          }
        });
      }
    });
  }

  unflagChat(chat: ChatData): void {
    if (!this.currentUserId) return;

    this.chatModerationService.unflagChat(chat.id, this.currentUserId).subscribe({
      next: () => {
        this.snackBar.open('Chat unflagged successfully', 'Close', { duration: 3000 });
        this.loadChats();
      },
      error: (err) => {
        this.snackBar.open('Failed to unflag chat', 'Close', { duration: 3000 });
        console.error('Error unflagging chat:', err);
      }
    });
  }

  disableUserChat(chat: ChatData): void {
    const dialogRef = this.dialog.open(DisableUserChatDialogComponent, {
      width: '400px',
      data: {
        recruiterName: chat.recruiterName,
        jobSeekerName: chat.jobSeekerName,
        recruiterId: chat.recruiterId,
        jobSeekerId: chat.jobSeekerId
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.currentUserId) {
        const userId = result.userId;
        this.chatModerationService.disableUserChat(userId, this.currentUserId, result.reason).subscribe({
          next: () => {
            this.snackBar.open('User chat disabled successfully', 'Close', { duration: 3000 });
            this.loadChats();
          },
          error: (err) => {
            this.snackBar.open('Failed to disable user chat', 'Close', { duration: 3000 });
            console.error('Error disabling user chat:', err);
          }
        });
      }
    });
  }

  viewChatHistory(chatId: number): void {
    this.chatModerationService.getChatMessages(chatId).subscribe({
      next: (messages) => {
        const messageText = messages.map(m => `${m.senderName}: ${m.content}`).join('\n');
        alert(`Chat History:\n\n${messageText}`);
      },
      error: (err) => {
        this.snackBar.open('Failed to load chat history', 'Close', { duration: 3000 });
        console.error('Error loading chat history:', err);
      }
    });
  }
}
