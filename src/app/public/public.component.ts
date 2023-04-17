import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSidenav} from '@angular/material/sidenav';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-public',
  templateUrl: './public.component.html',
  styleUrls: ['./public.component.css']
})
export class PublicComponent implements OnInit {
  
  @ViewChild('sidenav')
  sidenav!: MatSidenav;

  opened!: boolean;

  isLoggedIn = false;
  user:any;

  constructor(public loginService:LoginService,private router:Router) { }

  ngOnInit() {
    this.isLoggedIn=this.loginService.isLoggedIn();
    this.user=this.loginService.getUser();
    this.loginService.loginStatusSubject.asObservable().subscribe((data)=>{
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
      Swal.fire('Successfully Logout', '', 'success');
      this.router.navigateByUrl(`/public/login`);
     }else{
      Swal.fire(' Cancelled', '', 'error')
     }
    });
  }


  clickHandler() {
    this.sidenav.close();
  }

}