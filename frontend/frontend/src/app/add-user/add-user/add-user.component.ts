import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../service/user.service';  // Ensure correct path
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  standalone: true,  // Mark as standalone
  styleUrls: ['./add-user.component.css'],
  imports: [CommonModule, ReactiveFormsModule]  // Import CommonModule here for *ngFor
})
export class AddUserComponent {
  userForm: FormGroup;
  errorMessage: string = '';  // To store any error message

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    // Initialize the form with form controls for username and password
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Submit the form data
  onSubmit() {
    if (this.userForm.valid) {
      const newUser = this.userForm.value;  // Get the user data from the form

        // If token exists, pass it along with the request
        this.userService.createUser(newUser).subscribe(
          (data) => {
            console.log('User added successfully:', data);
            this.router.navigate(['/login']);  // Navigate to user list after adding
          },
          (error) => {
            console.error('Error adding user:', error);
            this.errorMessage = 'Error adding user: ' + error.message;
          }
        );
    } else {
      this.errorMessage = 'Please fill in the form correctly.';
    }
  }

  // Getter for easy access to form controls
  get f() {
    return this.userForm.controls;  // This allows access to form controls like f.username and f.password
  }
}
