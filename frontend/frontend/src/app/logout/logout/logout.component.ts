import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth-service.service'; // Import your AuthService
import { Router } from '@angular/router'; // Import Router for navigation

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Perform logout when this component is initialized
    this.logout();
  }

  logout(): void {
    console.log("Logging out...");
    this.authService.logout(); // Clear session

    // Delay navigation to login to ensure the session is cleared
    setTimeout(() => {
      this.router.navigate(['/login']); // Redirect to login page after logout
    }, 500); // 500 ms delay
  }
}
