import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class LoginComponent {
  loginForm = new FormGroup({
    username: new FormControl<string>(''),
    password: new FormControl<string>(''),
  });

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  onLogin() {
    if (this.loginForm.valid) {
      const username = this.loginForm.get('username')?.value ?? '';
      const password = this.loginForm.get('password')?.value ?? '';
  
      this.userService.login(username, password).subscribe({
        next: (response) => {
          if (response.token) {
            this.userService.storeToken(response.token);  // Store the JWT token
  
            // Ensure that the response contains a user object
            if (response.user) {
              localStorage.setItem('user', JSON.stringify(response.user));  // Store the user object
              console.log('User logged in:', response.user); // Debugging line
            } else {
              console.error('User data not found in response:', response);
              alert('Error: No user data received from the server');
              return;
            }
  
            this.router.navigate(['/weekplan']);  // Navigate to the weekplan after successful login
          } else {
            console.error('No token received from server');
            alert('Error: No token received from server');
          }
        },
        error: (error) => {
          console.error('Login failed:', error);
          alert('Login failed. Please check your credentials and try again.');
        }
      });
    }
  }
}