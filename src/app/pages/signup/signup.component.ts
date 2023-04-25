import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  constructor(private userService:UserService) { }

  public user = {
    firstName : '',
    lastName : '',
    email : '',
    phone : '',
    companyName : '',
    password:''
  }

  ngOnInit(): void {
  }

  formSubmit(){
    if(this.user.firstName == '' || this.user.firstName == null){
     Swal.fire({
       icon: 'error',
       title: 'Error !!',
       text: 'firstName is required !!'
     });
      return;
    }
    this.userService.addUser(this.user).subscribe(
      (data) => {
        this.user.firstName='';
        this.user.lastName='';
        this.user.email='';
        this.user.phone='';
        this.user.companyName='';
        this.user.password='';
        console.log(data);
       Swal.fire({
        icon: 'success',
        title: 'Successfully done !!',
        text: 'User is registered',
      });
      },
      (error) =>{
        console.log(error);
        Swal.fire({
         icon: 'error',
         title: 'Error !!',
         text: error.error
         
       });
      }
    )
   }

}
