import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CandidateSearchResult {
  candidateId: number;
  firstName: string;
  lastName: string;
  skills: string[];
  experience: number;
  location: string;
  relevanceScore: number;
  lastResumeUpdate: string;
  resumeFilePath: string;
}

export interface CandidateProfile {
  id: number;
  firstName: string;
  lastName: string;
  phone: string;
  location: string;
  summary: string;
  skills: string[];
  experiences: any[];
  educations: any[];
  resumeFilePath: string;
  resumeUploadedAt: string;
  relevanceScore: number;
}

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private apiUrl = `${environment.apiUrl}/candidates`;

  constructor(private http: HttpClient) { }

  searchCandidates(skills?: string[], experience?: string, location?: string): Observable<CandidateSearchResult[]> {
    let params = '';
    if (skills && skills.length > 0) {
      params += `skills=${skills.join('&skills=')}`;
    }
    if (experience) {
      params += (params ? '&' : '') + `experience=${experience}`;
    }
    if (location) {
      params += (params ? '&' : '') + `location=${location}`;
    }

    const url = params ? `${this.apiUrl}/search?${params}` : `${this.apiUrl}/search`;
    return this.http.get<CandidateSearchResult[]>(url);
  }

  getCandidateProfile(candidateId: number): Observable<CandidateProfile> {
    return this.http.get<CandidateProfile>(`${this.apiUrl}/${candidateId}/profile`);
  }
}
