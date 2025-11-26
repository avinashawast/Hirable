import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TalentPoolProfileDTO {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  location: string;
  summary: string;
  skills: string[];
  resumeFilePath: string;
  resumeUploadedAt: string;
  relevanceScore: number;
  flagged: boolean;
  flagReason: string;
  createdAt: string;
  updatedAt: string;
}

export interface FlagRequest {
  reason: string;
}

@Injectable({
  providedIn: 'root'
})
export class TalentPoolService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAllResumes(): Observable<TalentPoolProfileDTO[]> {
    return this.http.get<TalentPoolProfileDTO[]>(`${this.apiUrl}/resumes`);
  }

  getAllProfiles(): Observable<TalentPoolProfileDTO[]> {
    return this.http.get<TalentPoolProfileDTO[]>(`${this.apiUrl}/profiles`);
  }

  getProfile(jobSeekerId: number): Observable<TalentPoolProfileDTO> {
    return this.http.get<TalentPoolProfileDTO>(`${this.apiUrl}/profiles/${jobSeekerId}`);
  }

  flagProfile(jobSeekerId: number, reason: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/profiles/${jobSeekerId}/flag`, { reason });
  }

  unflagProfile(jobSeekerId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/profiles/${jobSeekerId}/unflag`, {});
  }

  removeProfile(jobSeekerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/profiles/${jobSeekerId}`);
  }
}
