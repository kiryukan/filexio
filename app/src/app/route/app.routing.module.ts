import { HomeComponent } from './../home/home.component';
import { DashboardComponent } from './../dashboard/dashboard.component';
import { AppComponent } from './../app.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { SignInComponent } from '../modal/sign-in/sign-in.component';
import { NotFoundComponent } from '../not-found/not-found.component';
import { AuthGuard } from '../service/auth-guard.service';

const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: 'dashboard', canActivate: [AuthGuard], component: DashboardComponent },
  { path: 'account', canActivate: [AuthGuard], component: SignInComponent },
  { path: 'file/:id', canActivate: [AuthGuard], component: SignInComponent},
  { path: '', component: HomeComponent},
  { path: 'not-found', component: NotFoundComponent},
  { path: '**', redirectTo: '/not-found'}
]; // sets up routes constant where you define your routes

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
