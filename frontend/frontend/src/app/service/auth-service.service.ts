// auth.service.ts
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from '../model/user';  // Assuming you have a User model

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {}

  // Mock implementation to get logged-in user
  // Replace with real authentication logic (e.g., token, session, etc.)
  getUser(): Observable<User> {
    const user: User = {
      userId: 21, 
      username: 'karina',
      password: 'karina'
    };  // Example user
    return of(user);
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('jwtToken') || sessionStorage.getItem('jwtToken');
    return !!token; // Return true if token exists, false otherwise
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user'); 
    sessionStorage.removeItem('jwtToken');
    console.log("Logged out successfully");
  }
}
