/* Modules */
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule} from '@angular/common/http';
import { AppRoutingModule } from './route/app.routing.module';
import {APP_BASE_HREF} from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
/* Components */
import { AppComponent } from './app.component';
import { UserComponent } from './user/user.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';
/* Services */
import { UserService } from './service/user.service';
import { AuthService } from './service/auth.service';
import { SharingService } from './service/sharing.service';
import { ModalComponent } from './modal/modal.component';
import { SigninFormComponent } from './form/signin-form/signin-form.component';
import { SignupFormComponent } from './form/signup-form/signup-form.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthGuard } from './service/auth-guard.service';
import { CreateFolderFormComponent } from './form/create-folder-form/create-folder-form.component';
import { SocialBarComponent } from './social-bar/social-bar.component';
import { StatsComponent } from './stats/stats.component';
import { AccountComponent } from './account/account.component';
import { StreamComponent } from './stream/stream.component';
import { AboutComponent } from './about/about.component';
import { DoughnutChartComponent } from './chart/doughnut-chart/doughnut-chart.component';
import { ModifyPasswordComponent } from './form/modify-password/modify-password.component';



@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    DashboardComponent,
    NavbarComponent,
    HomeComponent,
    ModalComponent,
    SigninFormComponent,
    SignupFormComponent,
    NotFoundComponent,
    CreateFolderFormComponent,
    SocialBarComponent,
    StatsComponent,
    AccountComponent,
    StreamComponent,
    AboutComponent,
    DoughnutChartComponent,
    ModifyPasswordComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    ChartsModule
  ],
  providers: [
    AuthService,
    UserService,
    SharingService,
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
