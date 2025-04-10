import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/v1/users';  // Your Spring Boot API URL
  private loginUrl = 'http://localhost:8080/api/v1/login'; // Login API URL

  constructor(private http: HttpClient) {}

  // Login method using Basic Authentication
  login(username: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(this.loginUrl, {
      username,
      password
    }).pipe(
      tap(response => {
        if (response.token) {
          this.storeToken(response.token);
        }
      })
    );
  }
  // Get all users (requires authentication)
  getAllUsers(): Observable<User[]> {
    const token = localStorage.getItem('authToken'); // Get the token from localStorage
    if (!token) {
      throw new Error('No authentication token found');
    }

    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.get<User[]>(this.apiUrl, { headers });
  }

  // Get user by ID (requires authentication)
  getUserById(userId: number): Observable<User> {
    const token = localStorage.getItem('authToken'); // Get the token from localStorage
    if (!token) {
      throw new Error('No authentication token found');
    }

    const url = `${this.apiUrl}/${userId}`;
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.get<User>(url, { headers });
  }

  // Create a new user (no authentication required)
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  // Update an existing user (requires authentication)
  updateUser(userId: number, user: User): Observable<User> {
    const token = localStorage.getItem('authToken'); // Get the token from localStorage
    if (!token) {
      throw new Error('No authentication token found');
    }

    const url = `${this.apiUrl}/${userId}`;
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.put<User>(url, user, { headers });
  }

  // Delete a user (requires authentication)
  deleteUser(userId: number): Observable<any> {
    const token = localStorage.getItem('authToken'); // Get the token from localStorage
    if (!token) {
      throw new Error('No authentication token found');
    }

    const url = `${this.apiUrl}/${userId}`;
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.delete(url, { headers });
  }

  // Store the JWT token in localStorage
  storeToken(token: string) {
    localStorage.setItem('authToken', token); // Store the JWT token
  }

  // Remove the JWT token when logging out
  removeToken() {
    localStorage.removeItem('authToken'); // Remove the JWT token
  }
}
