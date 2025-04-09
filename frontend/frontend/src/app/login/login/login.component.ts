import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,  // Mark as standalone
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {
  loginRequest = {
    username: '',
    password: '',
  };

  constructor(private http: HttpClient, private router: Router) {}

  onLogin() {
    const loginData = {
      username: this.loginRequest.username,
      password: this.loginRequest.password,
    };
    
    this.http
      .post('http://localhost:8080/api/v1/login', loginData)
      .subscribe(
        (response: any) => {
          // You can handle the login success here, maybe store the token in localStorage
          console.log('Login successful', response);
          // Redirect to the dashboard or some other page
          this.router.navigate(['/dashboard']);
          localStorage.setItem('authToken', response.token);  // Store JWT in localStorage
        },
        (error) => {
          // Handle login error (wrong credentials, etc.)
          console.error('Login failed', error);
          alert('Login failed. Please check your credentials and try again.');
        }
      );
  }
}
