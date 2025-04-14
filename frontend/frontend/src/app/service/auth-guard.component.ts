import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth-service.service'; // Adjust the path to where your AuthService is located

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    // Check if the user is logged in by checking token
    if (this.authService.isLoggedIn()) {
      return true; // Allow the route navigation
    } else {
      this.router.navigate(['/login']); // If not logged in, redirect to login
      return false; // Block the route navigation
    }
  }
}
