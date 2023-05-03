import { Component, ViewChild,OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './services/login.service';
import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  idleState = 'Not started.';
  timedOut = false;
  lastPing:any = null;
  title = 'centralizedNotificationFrontend';

  public modalRef!: BsModalRef;

  @ViewChild('childModal', { static: false })
  childModal!: ModalDirective;

  ngOnInit(): void {
  }

  constructor(private loginService:LoginService,private router:Router,private idle: Idle, private keepalive: Keepalive, private modalService: BsModalService,) {
    idle.setIdle(900); // this will be 15min(60*15=900s)

    idle.setTimeout(10); //counter time wii be 10s

    idle.setInterrupts(DEFAULT_INTERRUPTSOURCES); // there are various Interupts

    idle.onIdleEnd.subscribe(() => {
      this.idleState = 'No Longer Idle';
      this.reset();
    }); // this is the palce to do somthing if user comes back after being idle.

    idle.onTimeout.subscribe(() => {
      this.childModal.hide();
      this.idleState = 'Timed out!';
      this.timedOut = true;
      this.loginService.setUserLoggedIn(false);
      this.loginService.logout();
      this.router.navigate(['login']);
      window.location.reload();
    }); // this is place for redirect to login page.

    idle.onIdleStart.subscribe(() => {
      this.idleState = 'You have gone Idle';
      this.childModal.show();
    }); // this is place to do somthing when user goes Idle.

    idle.onTimeoutWarning.subscribe((countdown) => {
      this.idleState = countdown + 's';
    }); // this is the place for alert to notify of the logout

    keepalive.interval(15);

    keepalive.onPing.subscribe(() => {
      this.lastPing = new Date();
    });

    this.loginService.getUserLoggedIn().subscribe(userLoggedIn=>{
      if(userLoggedIn){
        this.idle.watch();
        this.timedOut = false;
      }else{
        this.idle.stop();
      }
    });
   }

   reset() {
    this.idle.watch();
    this.timedOut = false;
  }

  hideChildModal(): void {
    this.childModal.hide();
  }

  stay() {
    this.childModal.hide();
    this.reset();
  }

  logout() {
    this.childModal.hide();
    this.loginService.setUserLoggedIn(false);
    this.loginService.logout();
    this.router.navigate(['login']);
    window.location.reload();
  }
}
