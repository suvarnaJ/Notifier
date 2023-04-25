import { Injectable } from '@angular/core';
import { HttpClient,HttpEvent,HttpParams, HttpRequest } from '@angular/common/http';
import baseUrl from './helper';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { 
  }

  //Create a user
  public addUser(user:any){
    return this.http.post(`${baseUrl}/user/`,user);
  }

  //Upload a file
  public addFile(formData:any,userId:any){
     return this.http.post(`${baseUrl}/user/${userId}/upload-file`,formData);
  }

  //Get all template
  public getAllTemplates(userId:any,page:any,size:any){
   let params = new HttpParams();
   params = params.append('page', page);
   params = params.append('size', size);
     return this.http.get(`${baseUrl}/user/${userId}/get-all-templates`,{params: params});
  }

  //Delete template by id
  public deleteTemplateById(userId:any,templateId:any){
     return this.http.delete(`${baseUrl}/user/${userId}/delete-single-template/${templateId}`);
  }

  //Delete all template
  public deleteAllTemplates(userId:any){
    return this.http.delete(`${baseUrl}/user/${userId}/delete-all-templates`);
 }

   //Export template
   public exportTemplate(userId:any,templateName:any){
    return this.http.get(`${baseUrl}/user/${userId}/export-file/${templateName}`);
 }
}
