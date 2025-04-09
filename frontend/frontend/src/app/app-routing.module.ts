import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';  // Adjust the path as needed
import { AddUserComponent } from './add-user/add-user/add-user.component';
import { LoginComponent } from './login/login/login.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent },  // Define the route for users
  { path: 'adduser', component: AddUserComponent },  // New route for adding a user
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/users', pathMatch: 'full' },  // Default route
  // Add other routes here as needed
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
