import { Component,OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { LoginService } from 'src/app/services/login.service';
import { HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-import-template',
  templateUrl: './import-template.component.html',
  styleUrls: ['./import-template.component.css']
})

export class ImportTemplateComponent implements OnInit {

  ngOnInit(): void {
  }

  constructor(private UserService:UserService,private router:Router,private http: HttpClient,private loginService:LoginService) { }

  file:any;
  progress = { loaded : 0 , total : 0, disabled:true };

  selectFile(event:any){
    this.file = event.target.files[0];
    this.progress.loaded=50;
    this.progress.total=100;
    this.progress.disabled=false;
  }

  uploadFile(){
    let formData=new FormData();
    let user:any;
    formData.append('file',this.file);
    if(!(this.file.type=="text/html")){
      Swal.fire({
        icon: 'error',
        title: 'Error !!',
        text: "Only HTML content are allowed",
      });
    }else{
      user = this.loginService.getUser();
      this.UserService.addFile(formData,user.id).subscribe((data:any)=>{
            this.progress.loaded = 100;
            this.progress.total = 100;
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
              title: data.message
            })
            this.progress.disabled=true;
            setTimeout(() => {
              window.location.reload();
            }, 4000);
      },
      (error)=>{
        this.progress.loaded = 75;
        this.progress.total = 100;
        Swal.fire({
          icon: 'error',
          title: 'Error !!',
          text: error.error.message,
        });
      });
    }
  }

}
