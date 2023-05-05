import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

interface IUser {
  fullName: string;
  email: string;
  contactnumber: number;
  message: string;
  termsAndConditions: Boolean;
}

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent {

  userForm!: FormGroup;
  user: IUser;
  submitted = false;

  constructor(private router: Router) {
    this.user = {} as IUser;
  }

  ngOnInit(): void {
    this.userForm = new FormGroup({
      fullName: new FormControl(this.user.fullName, [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(20),
      ]),
      email: new FormControl(this.user.email, [
        Validators.required,
        Validators.email
      ]),
      contactnumber: new FormControl(this.user.contactnumber, [
        Validators.required,
        Validators.pattern("^((\\+91-?)|0)?[0-9]{10}$")
      ]),
      message: new FormControl(this.user.message, [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(200),
      ]),
      termsAndConditions: new FormControl(this.user.termsAndConditions, [
        Validators.required,
      ]),

    })
  }

  onSubmit = (user: any) => {
    this.submitted = true;
    // stop here if form is invalid
    if (this.userForm.invalid) {
      return;
    }
    Swal.fire({
      icon: 'success',
      title: 'Successfully done !!',
      text: 'Query Submitted',
    });
    this.reset();
  }

  get f() { return this.userForm.controls; }

  reset() {
    this.userForm.reset();
    this.userForm.controls['fullName'].setErrors(null);
    this.userForm.controls['email'].setErrors(null);
    this.userForm.controls['contactnumber'].setErrors(null);
    this.userForm.controls['message'].setErrors(null);
    this.userForm.controls['termsAndConditions'].setErrors(null);
    this.router.navigate(['/']);
  }

}
