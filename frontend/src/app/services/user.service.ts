import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserDTO {
  id: number;
  username: string;
  email: string;
  role: string;
  active: boolean;
  createdAt: string;
  lastLoginAt: string;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/${id}`);
  }

  createUser(request: CreateUserRequest): Observable<UserDTO> {
    return this.http.post<UserDTO>(this.apiUrl, request);
  }

  updateUser(id: number, user: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/${id}`, user);
  }

  deactivateUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activateUser(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/activate`, {});
  }

  getUsersByRole(role: string): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/role/${role}`);
  }

  getUsersByStatus(active: boolean): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/status/${active}`);
  }
}
