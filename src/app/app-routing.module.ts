import {Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { SignupComponent } from './pages/signup/signup.component';
import { LoginComponent } from './pages/login/login.component';
import { AboutComponent } from './pages/about/about.component';
import { ContactComponent } from './pages/contact/contact.component';
import { UserDashboardComponent } from './pages/user/user-dashboard/user-dashboard.component';
import { UserGuard } from './services/user.guard';
import { WelcomeDashboardComponent } from './pages/user/welcome-dashboard/welcome-dashboard.component';
import { ImportTemplateComponent } from './pages/user/import-template/import-template.component';
import { ExportTemplateComponent } from './pages/user/export-template/export-template.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { WelcomeComponent } from './pages/admin/welcome/welcome.component';
import { ShowTemplateComponent } from './pages/user/show-template/show-template.component';
import { ImportUserdefinedTemplateComponent } from './pages/user/import-userdefined-template/import-userdefined-template.component';
import { UserEmailComponent } from './pages/forgotPassword/user-email/user-email.component';
import { UserOtpComponent } from './pages/forgotPassword/user-otp/user-otp.component';
import { UserVerifyPasswordComponent } from './pages/forgotPassword/user-verify-password/user-verify-password.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'signup',
    component: SignupComponent,
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path:'about',
    component:AboutComponent,
    pathMatch: 'full',
  },
  {
    path:'contact',
    component:ContactComponent,
    pathMatch:'full'
  },
  {
    path : 'home',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path : 'forgot-password/send-otp',
    component: UserEmailComponent,
    pathMatch: 'full'
  },
  {
    path : 'forgot-password/send-otp/user-otp',
    component: UserOtpComponent,
    pathMatch: 'full'
  },
  {
    path : 'forgot-password/send-otp/user-otp/change-password',
    component: UserVerifyPasswordComponent,
    pathMatch: 'full'
  },
  {
    path: 'user-dashboard/:id',
    component: UserDashboardComponent,
    canActivate: [UserGuard],
    children: [
      {
        path:'',
        component: WelcomeDashboardComponent,
      },
      {
        path:'import-template',
        component:ImportTemplateComponent
      },
      {
        path: 'export-template',
        component:ExportTemplateComponent,
      },
      {
        path: 'show-template',
        component:ShowTemplateComponent
      },
      {
        path: 'import-userdefined-template',
        component:ImportUserdefinedTemplateComponent
      }
    ]
  },
  {
    path: 'admin-dashboard',
    component:AdminDashboardComponent,
    children:[
      {
        path:'',
        component:WelcomeComponent
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
