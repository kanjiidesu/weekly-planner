import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { AddUserComponent } from './add-user/add-user/add-user.component';
import { LoginComponent } from './login/login/login.component';
import { WeekplanComponent } from './weekplan/weekplan/weekplan.component';
import { LogoutComponent } from './logout/logout/logout.component';
import { AuthGuard } from './service/auth-guard.component';
import { PurchaseListComponent } from './purchase-list/purchase-list/purchase-list.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'adduser', component: AddUserComponent },
  { path: 'weekplan', component: WeekplanComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'purchaselist', component: PurchaseListComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' },  // Default route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
