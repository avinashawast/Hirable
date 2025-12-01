import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export enum JobStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  REMOVED = 'REMOVED'
}

export interface RecruiterDTO {
  id?: number;
  companyName?: string;
  firstName?: string;
  lastName?: string;
}

export interface JobDTO {
  id?: number;
  title: string;
  description: string;
  requiredSkills: string[];
  location: string;
  experienceLevel: string;
  industry: string;
  status?: JobStatus;
  postedAt?: string;
  approvedAt?: string;
  rejectionReason?: string;
  recruiter?: RecruiterDTO;
}

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getRecruiterJobs(recruiterId: number): Observable<JobDTO[]> {
    return this.http.get<JobDTO[]>(`${this.apiUrl}/recruiters/${recruiterId}/jobs`);
  }

  createJob(recruiterId: number, job: JobDTO): Observable<JobDTO> {
    return this.http.post<JobDTO>(`${this.apiUrl}/recruiters/${recruiterId}/jobs`, job);
  }

  updateJob(recruiterId: number, jobId: number, job: JobDTO): Observable<JobDTO> {
    return this.http.put<JobDTO>(`${this.apiUrl}/recruiters/${recruiterId}/jobs/${jobId}`, job);
  }

  deleteJob(recruiterId: number, jobId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/recruiters/${recruiterId}/jobs/${jobId}`);
  }

  getPendingJobs(): Observable<JobDTO[]> {
    return this.http.get<JobDTO[]>(`${this.apiUrl}/jobs/pending`);
  }

  approveJob(jobId: number): Observable<JobDTO> {
    return this.http.put<JobDTO>(`${this.apiUrl}/jobs/${jobId}/approve`, {});
  }

  rejectJob(jobId: number, reason: string): Observable<JobDTO> {
    return this.http.put<JobDTO>(`${this.apiUrl}/jobs/${jobId}/reject?reason=${reason}`, {});
  }

  getJobById(jobId: number): Observable<JobDTO> {
    return this.http.get<JobDTO>(`${this.apiUrl}/jobs/${jobId}`);
  }

  searchJobs(keyword: string = '', location?: string, industry?: string, experienceLevel?: string, page: number = 0): Observable<any> {
    let params = `keyword=${keyword}&page=${page}`;
    if (location) params += `&location=${location}`;
    if (industry) params += `&industry=${industry}`;
    if (experienceLevel) params += `&experienceLevel=${experienceLevel}`;
    return this.http.get<any>(`${this.apiUrl}/jobs?${params}`);
  }

  removeJob(jobId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/jobs/${jobId}`);
  }

  getAllJobs(): Observable<JobDTO[]> {
    return this.http.get<JobDTO[]>(`${this.apiUrl}/jobs/all`);
  }

  getJobsByStatus(status: string): Observable<JobDTO[]> {
    return this.http.get<JobDTO[]>(`${this.apiUrl}/jobs/status/${status}`);
  }
}
