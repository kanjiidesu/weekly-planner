// auth.service.ts
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { User } from '../model/user';  // Assuming you have a User model

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {}

  // Get logged-in user from localStorage
  getUser(): Observable<User | null> {
    const user = localStorage.getItem('user');  // Retrieve the user object from localStorage
    if (user) {
      try {
        return of(JSON.parse(user));  // Try to parse the user data and return it
      } catch (error) {
        console.error('Error parsing user data from localStorage:', error);
        return of(null);  // Return null if there's an error parsing
      }
    } else {
      console.warn('User not found in localStorage');
      return of(null);  // Return null if user is not found in localStorage
    }
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('jwtToken') || sessionStorage.getItem('jwtToken');
    return !!token; // Return true if token exists, false otherwise
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('token');
    localStorage.removeItem('user'); 
    localStorage.removeItem('lastListName');
    sessionStorage.removeItem('jwtToken');
    console.log("Logged out successfully");
  }
}
