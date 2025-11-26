import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TaxonomyDTO {
  id: number;
  name: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TaxonomyService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  // Category operations
  getAllCategories(): Observable<TaxonomyDTO[]> {
    return this.http.get<TaxonomyDTO[]>(`${this.apiUrl}/categories`);
  }

  getCategoryById(id: number): Observable<TaxonomyDTO> {
    return this.http.get<TaxonomyDTO>(`${this.apiUrl}/categories/${id}`);
  }

  createCategory(category: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.post<TaxonomyDTO>(`${this.apiUrl}/categories`, category);
  }

  updateCategory(id: number, category: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.put<TaxonomyDTO>(`${this.apiUrl}/categories/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/categories/${id}`);
  }

  // Industry operations
  getAllIndustries(): Observable<TaxonomyDTO[]> {
    return this.http.get<TaxonomyDTO[]>(`${this.apiUrl}/industries`);
  }

  getIndustryById(id: number): Observable<TaxonomyDTO> {
    return this.http.get<TaxonomyDTO>(`${this.apiUrl}/industries/${id}`);
  }

  createIndustry(industry: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.post<TaxonomyDTO>(`${this.apiUrl}/industries`, industry);
  }

  updateIndustry(id: number, industry: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.put<TaxonomyDTO>(`${this.apiUrl}/industries/${id}`, industry);
  }

  deleteIndustry(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/industries/${id}`);
  }

  // Skill operations
  getAllSkills(): Observable<TaxonomyDTO[]> {
    return this.http.get<TaxonomyDTO[]>(`${this.apiUrl}/skills`);
  }

  getSkillById(id: number): Observable<TaxonomyDTO> {
    return this.http.get<TaxonomyDTO>(`${this.apiUrl}/skills/${id}`);
  }

  createSkill(skill: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.post<TaxonomyDTO>(`${this.apiUrl}/skills`, skill);
  }

  updateSkill(id: number, skill: TaxonomyDTO): Observable<TaxonomyDTO> {
    return this.http.put<TaxonomyDTO>(`${this.apiUrl}/skills/${id}`, skill);
  }

  deleteSkill(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/skills/${id}`);
  }
}
