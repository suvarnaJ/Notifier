import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';
import { FormGroup, FormControl, Validators } from '@angular/forms';

interface IUser {
  username: string;
}

@Component({
  selector: 'app-user-email',
  templateUrl: './user-email.component.html',
  styleUrls: ['./user-email.component.css']
})
export class UserEmailComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;

  constructor(private userService: UserService, private router: Router) {
    this.user = {} as IUser;
  }

  ngOnInit(): void {
    this.userForm = new FormGroup({
      username: new FormControl(this.user.username, [
        Validators.required,
        Validators.email
      ])
    })
  }

  onSubmit = (user: any) => {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userService.sendOtp(user.username).subscribe(
      (response: any) => {
        this.router.navigate(['forgot-password/send-otp/user-otp']);
        localStorage.setItem("resetPasswordToken",response?.data?.resetPasswordToken);
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
          title: response?.message,
        })
      },
      (error) => {
        console.log(error);
        Swal.fire({
          icon: 'error',
          title: 'Something went wrong',
          text: error?.error?.message,
        });
      }
    );
  }

  get f() { return this.userForm.controls; }

}
