import { Component, OnInit, ViewChild } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {

  isLoggedIn = false;
  user:any;
  
  constructor(public loginService:LoginService,private router:Router) { }

  ngOnInit() {
    this.isLoggedIn=this.loginService.isLoggedIn();
    this.user=this.loginService.getUser();
    this.loginService.loginStatusSubject.asObservable().subscribe((data:any)=>{
      this.isLoggedIn=this.loginService.isLoggedIn();
      this.user=this.loginService.getUser();
    })
  }

  public logout(){
    Swal.fire({
      icon: 'warning',
      title: 'Are you sure ?',
      confirmButtonText: 'Logout',
      showCancelButton: true,
    }).then((result)=>{
     if(result.isConfirmed) {
      this.isLoggedIn=false;
      this.user=null;
      this.loginService.logout();
      Swal.fire({
        icon: 'success',
        title: 'Successfully Logout',
        text: '',
      });
      this.router.navigate(['login']);
     }else{
      Swal.fire({
        icon: 'error',
        title: 'Cancelled',
        text: '',
      });
     }
    });
  }

}
