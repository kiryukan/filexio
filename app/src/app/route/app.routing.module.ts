import { HomeComponent } from './../home/home.component';
import { DashboardComponent } from './../dashboard/dashboard.component';
import { StatsComponent } from '../stats/stats.component';
import { AccountComponent } from '../account/account.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; // CLI imports router
import { NotFoundComponent } from '../not-found/not-found.component';
import { AuthGuard } from '../service/auth-guard.service';
import { AboutComponent } from '../about/about.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: 'about', component: AboutComponent },
  { path: 'dashboard', canActivate: [AuthGuard], component: DashboardComponent },
  { path: 'stats', canActivate: [AuthGuard], component: StatsComponent },
  { path: 'account', canActivate: [AuthGuard], component: AccountComponent },
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
