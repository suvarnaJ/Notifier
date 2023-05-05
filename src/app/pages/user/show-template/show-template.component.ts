import { Component,OnInit,ViewChild, ElementRef } from '@angular/core';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-show-template',
  templateUrl: './show-template.component.html',
  styleUrls: ['./show-template.component.css'],
})

export class ShowTemplateComponent implements OnInit {

  templateData:any;

  ngOnInit(): void {
  }

  ngAfterViewInit() {
  }

  constructor(private loginService:LoginService,private userService:UserService,private router:Router) { }

}
