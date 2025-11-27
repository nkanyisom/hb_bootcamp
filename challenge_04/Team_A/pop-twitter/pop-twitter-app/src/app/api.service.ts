import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private baseUrl = 'http://localhost:8080';

  user: any

  constructor(private http: HttpClient) { }

  registerUser(username: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/register`, username);
  }

  getFeed(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/feed`);
  }

  createPost(userId: number, post: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/posts/${userId}`, post);
  }

  getPostsByUserId(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/posts/${userId}`);
  }

  setUser(user: any){ this.user = user}

  getUser(){return this.user}
}
