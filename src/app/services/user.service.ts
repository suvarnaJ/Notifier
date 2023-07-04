import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import baseUrl from './helper';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  //Create a user
  public addUser(user: any) {
    return this.http.post(`${baseUrl}/user/`, user);
  }

  //Upload a file
  public addFile(formData: any, userId: any) {
    return this.http.post(`${baseUrl}/user/${userId}/upload-file`, formData);
  }

  //Get all template
  public getAllTemplates(userId: any, page: any, size: any) {
    let params = new HttpParams();
    params = params.append('page', page);
    params = params.append('size', size);
    return this.http.get(`${baseUrl}/user/${userId}/get-all-templates`, { params: params });
  }

  //Delete template by id
  public deleteTemplateById(userId: any, templateId: any) {
    return this.http.delete(`${baseUrl}/user/${userId}/delete-single-template/${templateId}`);
  }

  //Delete all template
  public deleteAllTemplates(userId: any) {
    return this.http.delete(`${baseUrl}/user/${userId}/delete-all-templates`);
  }

  //Export template
  public exportTemplate(userId: any, templateName: any) {
    return this.http.get(`${baseUrl}/user/${userId}/export-file/${templateName}`);
  }

  //Get tokenStatus
  public getTokenStatus() {
    let params = new HttpParams();
    let tokenStr: any = localStorage.getItem('token');
    params = params.append('token', tokenStr);
    return this.http.get(`${baseUrl}/user/expireTokenStatus`, { params: params });
  }

  //Send OTP to server
  public sendOtp(email: string) {
    const queryParams = `?email=${email}`;
    return this.http.post(`${baseUrl}/user/send-otp` + queryParams, {});
  }

  //Verify OTP on server
  public verifyOtp(otp: any) {
    let resetPasswordToken = localStorage.getItem('resetPasswordToken');
    const queryParams = `?otp=${otp}&token=${resetPasswordToken}`;
    return this.http.post(`${baseUrl}/user/verify-otp` + queryParams, {});
  }

  //Change Password
  public changePassword(changePassword: any) {
    let resetPasswordToken = localStorage.getItem('resetPasswordToken');
    const queryParams = `?token=${resetPasswordToken}`;
    return this.http.post(`${baseUrl}/user/change-password` + queryParams,changePassword);
  }

    //Send a chatBotMessage
    public sendMessage(userId: any,message: any) {
      return this.http.get(`http://localhost:9091/user/${userId}/send/message/${message}`);
    }

}
