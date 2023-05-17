import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

interface IUser {
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

@Component({
  selector: 'app-user-verify-password',
  templateUrl: './user-verify-password.component.html',
  styleUrls: ['./user-verify-password.component.css']
})

export class UserVerifyPasswordComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;
  newPassword = '';
  confirmNewPassword = '';
  flag: boolean = false;
  showPassword: boolean=false;

  constructor(private router: Router, private fb: FormBuilder, private userService: UserService) {
    this.user = {} as IUser;
  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      oldPassword: new FormControl(this.user.oldPassword, [
        Validators.required,
        Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
      ]),
      newPassword: new FormControl(this.user.newPassword, [
        Validators.required,
        Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
      ]),
      confirmNewPassword: new FormControl(this.user.confirmNewPassword, [
        Validators.required,
        Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"),
      ]),
    });
  }

  onNewPasswordKey(event: any) {
    this.newPassword = event.target.value;
    this.flag = false;
  }

  onConfirmNewPasswordKey(event: any) {
    this.confirmNewPassword = event.target.value;
    this.flag = true;
  }

  onSubmit = (user: any) => {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }
    this.userService.changePassword(user).subscribe(
      (response: any) => {
        this.router.navigate(['login']);
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

  doAction(event:any){
    this.showPassword=event.target.checked;
  }

}


