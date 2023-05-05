import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-sidenav',
  templateUrl: './user-sidenav.component.html',
  styleUrls: ['./user-sidenav.component.css']
})

export class UserSidenavComponent {
  
  user:any;

  constructor(private loginService:LoginService,private router:Router) { }

  ngOnInit(): void {
    this.user = this.loginService.getUser();
  }

}
