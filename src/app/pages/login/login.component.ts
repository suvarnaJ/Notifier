import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginData = {
    username: '',
    password: '',
  };

  constructor(private loginService:LoginService,private router:Router) { }

  ngOnInit(): void {
  }

  formSubmit(){

    if(this.loginData.username.trim()=='' || this.loginData.username==null){
      Swal.fire({
        icon: 'error',
        title: 'Error !!',
        text: 'Email is required !!'
      });
       return;
    }

    if(this.loginData.password.trim()=='' || this.loginData.password==null){
      Swal.fire({
        icon: 'error',
        title: 'Error !!',
        text: 'Password is required !!'
      });
       return;
    }

    //request to server to generate token
    this.loginService.generateToken(this.loginData).subscribe(
      (data:any)=>{
        console.log('Success');
        console.log(data);

        this.loginService.loginUser(data.token);

        this.loginService.getCurrentUser().subscribe(
          (user:any)=>{
            this.loginService.setUser(user);
            if(this.loginService.getUserRole() == 'ADMIN'){
              this.router.navigate(['admin']);
              this.loginService.loginStatusSubject.next(true);
            }else if (this.loginService.getUserRole() == 'NORMAL'){
              this.router.navigate(['user-dashboard/'+user.id]);
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
            }else{
              this.loginService.logout();
            }
          }
        )

      },
      (error)=>{
        console.log("Error !");
        console.log(error);
        Swal.fire({
          icon: 'error',
          title: 'Error !!',
          text: 'Invalid Details !! Try Again'
        });
      }
    );
  }

}
