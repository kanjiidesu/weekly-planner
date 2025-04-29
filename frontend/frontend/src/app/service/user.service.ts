import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://86.52.114.30:8081/api/v1/users';
  // private apiUrl = 'http://localhost:8080/api/v1/users'; not this
  //private apiUrl = 'http://localhost:8081/api/v1/users';

  private loginUrl = 'http://86.52.114.30:8081/api/v1/login';
  //private loginUrl = 'http://localhost:8081/api/v1/login';
  // private loginUrl = 'http://localhost:8080/api/v1/login'; not this

  constructor(private http: HttpClient) {}

  // Login method using Basic Authentication
  login(username: string, password: string): Observable<{ token: string; user: User }> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
  
    return this.http.post<{ token: string; user: User }>(
      this.loginUrl,
      { username, password },
      { headers, withCredentials: true }
    ).pipe(
      tap(response => {
        if (response.token) {
          this.storeToken(response.token);
          this.storeUser(response.user);
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
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
  
    return this.http.post<User>(
      this.apiUrl,
      user, // <-- no JSON.stringify()
      {
        headers,
        withCredentials: true
      }
    );
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
    localStorage.setItem('jwtToken', token);
  }

  // Remove the JWT token when logging out
  removeToken() {
    localStorage.removeItem('authToken'); // Remove the JWT token
  }

  storeUser(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }
  
  // Retrieve the stored user from localStorage
  getStoredUser(): User | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
