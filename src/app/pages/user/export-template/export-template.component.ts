import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { LoginService } from 'src/app/services/login.service';
import { PagingConfig } from 'src/app/services/paging-config.model';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-export-template',
  templateUrl: './export-template.component.html',
  styleUrls: ['./export-template.component.css']
})
export class ExportTemplateComponent implements OnInit {

  user:any;
  templatesData:any;
  templateData:any;
  currentPage:number  = 0;
  itemsPerPage: number = 5;
  totalItems: number = 0;

  pagingConfig: PagingConfig = {} as PagingConfig;

  constructor(private loginService:LoginService,private userService:UserService,private router:Router) { }

  ngOnInit(): void {
    this.user = this.loginService.getUser();
    this.fetchAllTemplate();
    this.pagingConfig = {
      itemsPerPage: this.itemsPerPage,
      currentPage: this.currentPage,
      totalItems: this.totalItems
    }
  }

  onTableDataChange(event:any){
    this.pagingConfig.currentPage  = event;
    this.currentPage = event-1
    this.fetchAllTemplate();
  }

  fetchAllTemplate(){
    this.userService.getAllTemplates(this.user.id,this.currentPage,this.itemsPerPage).subscribe((payload:any)=>{
      this.templatesData = payload.data;
      this.pagingConfig.totalItems = this.templatesData.totalElements;
    },error=>{
      Swal.fire({
        icon: 'error',
        title: 'Something went wrong',
        text: error.error.message,
      });
    });
  }


  deleteFile(templateId:any){

    Swal.fire({
      icon: 'warning',
      title: 'Are you sure ?',
      text: "You won't be able to revert this!",
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
      showCancelButton: true,
    }).then((result)=>{
     if(result.isConfirmed) {
      this.userService.deleteTemplateById(this.user.id,templateId).subscribe((data:any)=>{
        Swal.fire({
          icon: 'success',
          title: 'Successfully Deleted',
          text: data.message,
        });
        this.fetchAllTemplate();
        this.onTableDataChange(1);
      },error=>{
        console.log(error);
        Swal.fire({
          icon: 'error',
          title: 'Something went wrong',
          text: error.error.message,
        });
      });
     }else{
      Swal.fire({
        icon: 'error',
        title: 'Cancelled',
        text: 'Your template is safe',
      });
     }
    });
  }


  deleteAllFile(){
    Swal.fire({
      icon: 'warning',
      title: 'Are you sure ?',
      text: "You won't be able to revert this!",
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
      showCancelButton: true,
    }).then((result)=>{
     if(result.isConfirmed) {
      this.userService.deleteAllTemplates(this.user.id).subscribe((data:any)=>{
        Swal.fire({
          icon: 'success',
          title: 'Successfully Deleted',
          text: data.message,
        });
        this.onTableDataChange(1);
        this.fetchAllTemplate();
      },error=>{
        console.log(error);
        Swal.fire({
          icon: 'error',
          title: 'Something went wrong',
          text: error.error.message,
        });
      });
     }else{
      Swal.fire({
        icon: 'error',
        title: 'Cancelled',
        text: 'Your template is safe',
      });
     }
    });
  }


viewFile(userTemplate:any){
  this.userService.exportTemplate(this.user.id,userTemplate).subscribe((data:any)=>{
    this.templateData = data.data;
    this.openWindow();
  },(error=>{
    console.log(error);
  }));
}

openWindow() {  
  let newtab:any = window.open("", "anotherWindow", "width=auto,height=auto");  
  newtab.document.write(this.templateData);  
  }  

}
