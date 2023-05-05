import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';
import { FormGroup, FormControl,Validators } from '@angular/forms';
import { Router } from '@angular/router';

interface IUser {
  firstName: string;
  lastName: string;
  email: string;
  phone: number;
  companyName: string;
  password: string;
  termsAndConditions:Boolean;
}

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})

export class SignupComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;

  constructor(private userService:UserService,private router: Router) {
    this.user = {} as IUser;
  }

  ngOnInit(): void {
     this.userForm = new FormGroup({
      firstName: new FormControl(this.user.firstName,[
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(10),
      ]),
      lastName: new FormControl(this.user.lastName,[
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(10),
      ]),
      email: new FormControl(this.user.email,[
        Validators.required,
        Validators.email
      ]),
      phone: new FormControl(this.user.phone,[
        Validators.required,
        Validators.pattern("^((\\+91-?)|0)?[0-9]{10}$")
      ]),
      companyName: new FormControl(this.user.companyName,[
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(10),
      ]),
      password: new FormControl(this.user.password,[
        Validators.required,
        Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
      ]),
      termsAndConditions: new FormControl(this.user.termsAndConditions,[
        Validators.required,
      ]),

    })
  }

  onSubmit = (user:any) => {
    this.submitted = true;
     // stop here if form is invalid
     if (this.userForm.invalid) {
      return;
  }
    this.userService.addUser(user).subscribe(
      (data) => {
       Swal.fire({
        icon: 'success',
        title: 'Successfully done !!',
        text: 'User is registered',
      });
      this.reset();
      },
      (error) =>{
        console.log(error);
        Swal.fire({
         icon: 'error',
         title: 'Something went wrong',
         text: error.error 
       });
      }
    )
  }

  get f() { return this.userForm.controls; }

  reset(){
    this.userForm.reset();
    this.userForm.controls['firstName'].setErrors(null);
    this.userForm.controls['lastName'].setErrors(null);
    this.userForm.controls['email'].setErrors(null);
    this.userForm.controls['phone'].setErrors(null);
    this.userForm.controls['companyName'].setErrors(null);
    this.userForm.controls['password'].setErrors(null);
    this.userForm.controls['termsAndConditions'].setErrors(null);
    this.router.navigate(['login']);
  }

}
