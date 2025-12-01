import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ExperienceDTO {
  id?: number;
  company: string;
  title: string;
  startDate?: string;
  endDate?: string;
  description: string;
}

export interface EducationDTO {
  id?: number;
  institution: string;
  degree: string;
  fieldOfStudy: string;
  graduationDate?: string;
}

export interface JobSeekerProfileDTO {
  id?: number;
  firstName: string;
  lastName: string;
  phone: string;
  location: string;
  summary: string;
  skills: string[];
  experiences: ExperienceDTO[];
  educations: EducationDTO[];
  resumeFilePath?: string;
  resumeUploadedAt?: string;
  relevanceScore?: number;
}

@Injectable({
  providedIn: 'root'
})
export class JobSeekerService {
  private apiUrl = `${environment.apiUrl}/jobseekers`;

  constructor(private http: HttpClient) {}

  getProfile(jobSeekerId: number): Observable<JobSeekerProfileDTO> {
    return this.http.get<JobSeekerProfileDTO>(`${this.apiUrl}/${jobSeekerId}/profile`);
  }

  updateProfile(jobSeekerId: number, profile: JobSeekerProfileDTO): Observable<JobSeekerProfileDTO> {
    return this.http.put<JobSeekerProfileDTO>(`${this.apiUrl}/${jobSeekerId}/profile`, profile);
  }

  uploadResume(jobSeekerId: number, file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(`${this.apiUrl}/${jobSeekerId}/resume`, formData);
  }

  applyToJob(jobSeekerId: number, jobId: number): Observable<ApplicationDTO> {
    return this.http.post<ApplicationDTO>(`${this.apiUrl}/${jobSeekerId}/applications?jobId=${jobId}`, {});
  }

  getApplications(jobSeekerId: number): Observable<ApplicationDTO[]> {
    return this.http.get<ApplicationDTO[]>(`${this.apiUrl}/${jobSeekerId}/applications`);
  }
}

export interface ApplicationDTO {
  id?: number;
  jobSeekerId: number;
  jobId: number;
  jobTitle: string;
  companyName: string;
  status: string;
  appliedAt: string;
}
