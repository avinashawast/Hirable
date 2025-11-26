import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AnalyticsDTO {
  totalJobSeekers: number;
  totalRecruiters: number;
  totalAdmins: number;
  totalJobs: number;
  approvedJobs: number;
  pendingJobs: number;
  rejectedJobs: number;
  totalApplications: number;
  totalChats: number;
  totalMessages: number;
  totalNotifications: number;
  recruiterActivity: RecruiterActivityDTO;
  jobSeekerActivity: JobSeekerActivityDTO;
  chatActivity: ChatActivityDTO;
}

export interface RecruiterActivityDTO {
  totalJobsPosted: number;
  jobsApproved: number;
  jobsRejected: number;
  candidatesContacted: number;
}

export interface JobSeekerActivityDTO {
  totalApplicationsSubmitted: number;
  profilesWithResumes: number;
  averageApplicationsPerSeeker: number;
}

export interface ChatActivityDTO {
  totalChats: number;
  totalMessages: number;
  averageMessagesPerChat: number;
}

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl = 'http://localhost:8080/api/admin/analytics';

  constructor(private http: HttpClient) {}

  getAnalytics(): Observable<AnalyticsDTO> {
    return this.http.get<AnalyticsDTO>(this.apiUrl);
  }
}
