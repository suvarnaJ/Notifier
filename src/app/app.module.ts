import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { authInterceptorProviders } from './services/auth.interceptor';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { AboutComponent } from './pages/about/about.component';
import { ContactComponent } from './pages/contact/contact.component';
import { UserDashboardComponent } from './pages/user/user-dashboard/user-dashboard.component';
import { ImportTemplateComponent } from './pages/user/import-template/import-template.component';
import { ExportTemplateComponent } from './pages/user/export-template/export-template.component';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { WelcomeComponent } from './pages/admin/welcome/welcome.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard/admin-dashboard.component';
import { UserSidenavComponent } from './pages/user/user-sidenav/user-sidenav.component';
import { WelcomeDashboardComponent } from './pages/user/welcome-dashboard/welcome-dashboard.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ShowTemplateComponent } from './pages/user/show-template/show-template.component';
import { ImportUserdefinedTemplateComponent } from './pages/user/import-userdefined-template/import-userdefined-template.component';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ReactiveFormsModule } from '@angular/forms';
import {
  NgxUiLoaderModule,
  NgxUiLoaderRouterModule,
  NgxUiLoaderHttpModule
} from 'ngx-ui-loader';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    NavbarComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    AboutComponent,
    ContactComponent,
    UserDashboardComponent,
    ImportTemplateComponent,
    ExportTemplateComponent,
    WelcomeComponent,
    AdminDashboardComponent,
    UserSidenavComponent,
    WelcomeDashboardComponent,
    ShowTemplateComponent,
    ImportUserdefinedTemplateComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    NgxPaginationModule,
    AngularEditorModule,
    NgIdleKeepaliveModule.forRoot(),
    ModalModule.forRoot(),
    ReactiveFormsModule,
    NgxUiLoaderModule.forRoot({
      text:"Please wait...",
    }),
    NgxUiLoaderRouterModule.forRoot({
      showForeground:true,
    }),
    NgxUiLoaderHttpModule.forRoot({
      showForeground:true,
    }),
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
