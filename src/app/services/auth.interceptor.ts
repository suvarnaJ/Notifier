import { HttpEvent,HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from "@angular/common/http";
import { Injectable } from "@angular/core";
import {Observable, throwError} from 'rxjs';
import {catchError} from "rxjs/operators";
import { LoginService } from "./login.service";
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable()
export class AuthInterceptor implements HttpInterceptor{

    constructor(private loginService:LoginService,private router:Router){ }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
        ): Observable<HttpEvent<any>> {

            let authReq = req;
            const token=this.loginService.getToken();
            if(token!=null){
                authReq = authReq.clone({
                    setHeaders: { Authorization: `Bearer ${token}` },
                });
            }
            return next.handle(authReq);
    }
    
}

export const authInterceptorProviders =[
    {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true,
    },
];