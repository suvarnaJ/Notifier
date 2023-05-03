import { Component,OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { LoginService } from 'src/app/services/login.service';
import { HttpClient} from '@angular/common/http';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-import-userdefined-template',
  templateUrl: './import-userdefined-template.component.html',
  styleUrls: ['./import-userdefined-template.component.css']
})
export class ImportUserdefinedTemplateComponent implements OnInit {

  ngOnInit(): void {
  }

  constructor(private UserService:UserService,private router:Router,private http: HttpClient,private loginService:LoginService) { }

  progress = { loaded : 0 , total : 0, disabled:true };
  htmlContent = '';
  file:any;
  date = new Date();
  config: AngularEditorConfig = {
    editable: true,
      spellcheck: true,
      height: 'auto',
      minHeight: '15rem',
      maxHeight: 'auto',
      width: 'auto',
      minWidth: '0',
      translate: 'yes',
      enableToolbar: true,
      showToolbar: true,
      placeholder: 'Enter text here...',
      defaultParagraphSeparator: '',
      defaultFontName: '',
      defaultFontSize: '',
      fonts: [
        {class: 'arial', name: 'Arial'},
        {class: 'times-new-roman', name: 'Times New Roman'},
        {class: 'calibri', name: 'Calibri'},
        {class: 'comic-sans-ms', name: 'Comic Sans MS'}
      ],
      customClasses: [
      {
        name: 'quote',
        class: 'quote',
      },
      {
        name: 'redText',
        class: 'redText'
      },
      {
        name: 'titleText',
        class: 'titleText',
        tag: 'h1',
      },
    ],
};


formSubmit(){
  let formData=new FormData();
  let user:any;
  formData.append('file',this.file);
  if(!(this.file.type=="text/html")){
    Swal.fire({
      icon: 'error',
      title: 'Something went wrong',
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
            if(this.progress.loaded==100 && this.progress.total==100){
              this.progress.loaded = 0;
              this.progress.total = 0;
            }
          }, 4000);
    },
    (error)=>{
      this.progress.loaded = 75;
      this.progress.total = 100;
      Swal.fire({
        icon: 'error',
        title: 'Something went wrong',
        text: error.error.message,
      });
    });
  }
}

writeContents(content:any, fileName:any, contentType:any) {
  var a = document.createElement('a');
  var file = new Blob([content], {type: contentType});
  a.href = URL.createObjectURL(file);
  a.download = fileName;
  a.click();
  this.htmlContent='';
}

selectFile(event:any){
  this.file = event.target.files[0];
  event.target.value="";
  this.progress.loaded=50;
  this.progress.total=100;
  this.progress.disabled=false;
}

saveAsFile(){
  if(this.htmlContent==''){
    Swal.fire({
      icon: 'error',
      title: 'Error !!',
      text: "Field can't be null",
    });
  }else{
    this.writeContents(this.htmlContent, `Template_${this.date.getTime()}`+'.html', 'text/html');
  }
}

}
