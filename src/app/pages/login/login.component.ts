import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';
import { FormGroup, FormControl,Validators } from '@angular/forms';

interface IUser {
  username: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;

  constructor(private loginService: LoginService, private router: Router) {
    this.user = {} as IUser;
   }

  ngOnInit(): void {
    this.userForm = new FormGroup({
      username: new FormControl(this.user.username,[
        Validators.required,
        Validators.email
      ]),
      password: new FormControl(this.user.password,[
        Validators.required,
        Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
      ]),
    })
  }

  onSubmit = (user:any) => {
    this.submitted = true;
     // stop here if form is invalid
     if (this.userForm.invalid) {
      return;
    }
       //request to server to generate token
       this.loginService.generateToken(user).subscribe(
        (data: any) => {
          console.log(new Date(data.tokenExpiry));
          this.loginService.loginUser(data.token);
          this.loginService.getCurrentUser().subscribe(
            (user: any) => {
              this.loginService.setUser(user);
              if (this.loginService.getUserRole() == 'ADMIN') {
                this.router.navigate(['admin']);
                this.loginService.loginStatusSubject.next(true);
              } else if (this.loginService.getUserRole() == 'NORMAL') {
                this.router.navigate(['user-dashboard/' + user.id]);
                this.loginService.loginStatusSubject.next(true);
                const Toast = Swal.mixin({
                  toast: true,
                  position: 'top-end',
                  showConfirmButton: false,
                  timer: 3000,
                  timerProgressBar: true,
                  didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                  }
                })
                Toast.fire({
                  icon: 'success',
                  title: 'Signed in successfully'
                })
              } else {
                this.loginService.logout();
              }
            }
          )
        },
        (error) => {
          console.log("Error !");
          console.log(error);
          Swal.fire({
            icon: 'error',
            title: 'Something went wrong',
            text: 'Invalid Details !! Try Again'
          });
        }
      );
  }

  get f() { return this.userForm.controls; }

}
