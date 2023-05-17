import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

interface IUser {
  otp: number;
}

@Component({
  selector: 'app-user-otp',
  templateUrl: './user-otp.component.html',
  styleUrls: ['./user-otp.component.css']
})

export class UserOtpComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;

  constructor(private router: Router, private userService: UserService) {
    this.user = {} as IUser;
  }

  ngOnInit(): void {
    this.userForm = new FormGroup({
      otp: new FormControl(this.user.otp, [
        Validators.required,
        Validators.pattern("^((\\+91-?)|0)?[0-9]{6}$")
      ]),
    })
  }

  onSubmit = (user: any) => {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userService.verifyOtp(user.otp).subscribe(
      (response: any) => {
        this.router.navigate(['forgot-password/send-otp/user-otp/change-password']);
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
          title: response.message,
        })
      },
      (error:any) => {
        Swal.fire({
          icon: 'error',
          title: 'Something went wrong',
          text: error.error.message,
        });
      }
    );
  }

  get f() { return this.userForm.controls; }

}
