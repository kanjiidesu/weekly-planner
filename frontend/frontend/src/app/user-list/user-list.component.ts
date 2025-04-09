import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';  // Ensure correct path
import { User } from '../model/user';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  imports: [CommonModule]
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  token: string | null = localStorage.getItem('authToken');  // Get token from localStorage

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    console.log('Token retrieved:', this.token);  // Add this log

      this.userService.getAllUsers().subscribe(
        (data) => {
          this.users = data;  // Populate the users list
        },
        (error) => {
          console.error('Error fetching users:', error);
        }
      );
  }  
}
