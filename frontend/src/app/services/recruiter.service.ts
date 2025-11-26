import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RecruiterProfileDTO {
  id?: number;
  firstName: string;
  lastName: string;
  phone: string;
  companyName: string;
  companyWebsite: string;
  companyDescription: string;
  industry: string;
}

export interface ShortlistDTO {
  id: number;
  recruiterId: number;
  jobSeekerId: number;
  firstName: string;
  lastName: string;
  location: string;
  summary: string;
  skills: string[];
  addedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class RecruiterService {
  private apiUrl = 'http://localhost:8080/api/recruiters';

  constructor(private http: HttpClient) {}

  getProfile(recruiterId: number): Observable<RecruiterProfileDTO> {
    return this.http.get<RecruiterProfileDTO>(`${this.apiUrl}/${recruiterId}/profile`);
  }

  updateProfile(recruiterId: number, profile: RecruiterProfileDTO): Observable<RecruiterProfileDTO> {
    return this.http.put<RecruiterProfileDTO>(`${this.apiUrl}/${recruiterId}/profile`, profile);
  }

  getShortlist(recruiterId: number): Observable<ShortlistDTO[]> {
    return this.http.get<ShortlistDTO[]>(`${this.apiUrl}/${recruiterId}/shortlist`);
  }

  addToShortlist(recruiterId: number, candidateId: number): Observable<ShortlistDTO> {
    return this.http.post<ShortlistDTO>(`${this.apiUrl}/${recruiterId}/shortlist?candidateId=${candidateId}`, {});
  }

  removeFromShortlist(recruiterId: number, candidateId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${recruiterId}/shortlist/${candidateId}`);
  }
}
