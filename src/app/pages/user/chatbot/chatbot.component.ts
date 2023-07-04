import { Component ,OnInit, Renderer2, ElementRef,ViewChild} from '@angular/core';
import { FormGroup,FormsModule,FormBuilder } from '@angular/forms';
import {Chatbox} from '../../../services/chatbox.service';
declare var jquery:any;
declare var $:any;
import * as bootstrap from 'bootstrap';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit {

  modalName: bootstrap.Modal | undefined;
  chatBotModal:any;

  TextMsg:any;
  chatModal=new Chatbox("Say hi");
  sendButton:boolean;
  bottext:any;
  initialBotDate:any;
  flag:any=true;
  @ViewChild('chatlogs',{ read: ElementRef, static: false }) divMsgs: any;
  @ViewChild('chatlogs',{ read: ElementRef, static: false }) botMsgs: any;

  constructor(private formBuilder:FormBuilder,private renderer:Renderer2,private userService:UserService){
    this.sendButton=true
  }

  openModal(element:any){
    this.chatBotModal = new bootstrap.Modal(element,{} ) 
    this.chatBotModal?.show();
    setTimeout(() => {
      this.initialBotDate = this.formatAMPM(new Date());
      let audio = new Audio();
      if(this.flag==true){
        audio.src = "../../../assets/audio/src_assets_audio_tick.mp3";
        audio.load();
        audio.play();
      }
      this.flag=false;
    }, 3000);
  }

  closeModal(){
    this.chatBotModal?.hide();
    window.location.reload();
  }

  ngOnInit(){
    $("#close").hover(
      function () {
        $("#chatdone").show();
      }, 
      function () {
        $("#chatdone").hide();
      }
    );
  }
  title = 'ChatBotApp';
  Empty(){
    if(this.chatModal.inputQuery!=null){
      this.sendButton=true  
    }
    if(this.chatModal.inputQuery==null){
      this.sendButton=false
    }
  }
  onSubmit(){
    this.sendButton=false
    if(this.chatModal.inputQuery==""){
      return false
    }else{
      //User Msgs
      const divMain= this.renderer.createElement('div');
      const userimg= this.renderer.createElement('div');
      this.renderer.addClass(userimg,"userimg");
      const divSub= this.renderer.createElement('div');
      const text=this.renderer.createText(this.chatModal.inputQuery);
      this.renderer.appendChild(divSub,userimg);
      this.renderer.appendChild(divSub,text);
      this.renderer.addClass(divSub,"UserMsg");  
      this.renderer.appendChild(divMain,divSub);
      this.renderer.addClass(divMain,"d-flex");
      this.renderer.addClass(divMain,"flex-row-reverse");
      this.renderer.appendChild(this.divMsgs.nativeElement,divMain);

      //User Time Msgs
      const userTimeDivMain= this.renderer.createElement('div');
      const userTimeDivSub= this.renderer.createElement('div');
      const userTimeText=this.renderer.createText(this.formatAMPM(new Date()));
      this.renderer.appendChild(userTimeDivSub,userTimeText);
      this.renderer.addClass(userTimeDivSub,"UsertimeMsg");  
      this.renderer.appendChild(userTimeDivMain,userTimeDivSub);
      this.renderer.addClass(userTimeDivMain,"d-flex");
      this.renderer.addClass(userTimeDivMain,"flex-row-reverse");
      this.renderer.appendChild(this.divMsgs.nativeElement,userTimeDivMain);

      var out:any = document.getElementById("chatlogs");
      var isScrolledToBottom = out.scrollHeight - out.clientHeight <= out.scrollTop;
      console.log(isScrolledToBottom)
      if(!isScrolledToBottom){
        out.scrollTop = out.scrollHeight - out.clientHeight;
      }

      //Audio click
      let audio = new Audio();
      audio.src = "../../../assets/audio/src_assets_audio_tick.mp3";
      audio.load();
      audio.play();
      

      //Waiting Time
      const ballParent = this.renderer.createElement('div');
      const ballChild = this.renderer.createElement('div');
      const ball1Span = this.renderer.createElement('div');
      const ball2Span = this.renderer.createElement('div');
      const ball3Span = this.renderer.createElement('div');

      this.typing(true,ballParent,ballChild,ball1Span,ball2Span,ball3Span);

      //Bot Msgs
      this.userService.sendMessage(12,this.chatModal.inputQuery).subscribe((response:any)=>{

        this.typing(false,ballParent,ballChild,ball1Span,ball2Span,ball3Span);

        const botMain= this.renderer.createElement('div');
        const botimg= this.renderer.createElement('div');
        this.renderer.addClass(botimg,"botimg"); 
        const botSub= this.renderer.createElement('div');
        this.bottext=this.renderer.createText(response?.data);
        this.renderer.appendChild(botSub,botimg);
        this.renderer.appendChild(botSub,this.bottext);
        this.renderer.addClass(botSub,"botMsg");  
        this.renderer.appendChild(botMain,botSub);
        this.renderer.addClass(botMain,"d-flex");
        this.renderer.appendChild(this.divMsgs.nativeElement,botMain);

        const botTimeDivMain= this.renderer.createElement('div');
        const botTimeDivSub= this.renderer.createElement('div');
        const botTimeText=this.renderer.createText(this.formatAMPM(new Date()));
        this.renderer.appendChild(botTimeDivSub,botTimeText);
        this.renderer.addClass(botTimeDivSub,"BottimeMsg");  
        this.renderer.appendChild(botTimeDivMain,botTimeDivSub);
        this.renderer.addClass(botTimeDivMain,"d-flex");
        this.renderer.appendChild(this.divMsgs.nativeElement,botTimeDivMain);
  
        var out:any = document.getElementById("chatlogs");
        var isScrolledToBottom = out.scrollHeight - out.clientHeight <= out.scrollTop + 1;
        console.log(isScrolledToBottom)
        if(!isScrolledToBottom)
            out.scrollTop = out.scrollHeight - out.clientHeight;
  
        //Audio click
        let audio = new Audio();
        audio.src = "../../../assets/audio/src_assets_audio_tick.mp3";
        audio.load();
        audio.play();
        this.chatModal.inputQuery="" //Reseting to empty for next query
      },(error)=>{
        Swal.fire({
          icon: 'error',
          title: 'Something went wrong',
          text: error.error.message
        });
        this.chatBotModal?.hide();
      });
     
    }
    return;
  }

typing(flag:any,ballParent:any,ballChild:any,ball1Span:any,ball2Span:any,ball3Span:any){
  if(flag){
      this.renderer.appendChild(ballChild,ball1Span);
      this.renderer.appendChild(ballChild,ball2Span);
      this.renderer.appendChild(ballChild,ball3Span);
      this.renderer.appendChild(ballParent,ballChild);
      this.renderer.addClass(ballParent,"chat-bubble"); 
      this.renderer.addClass(ballChild,"typing");
      this.renderer.addClass(ball1Span,"dot"); 
      this.renderer.addClass(ball2Span,"dot"); 
      this.renderer.addClass(ball3Span,"dot"); 
      this.renderer.appendChild(this.divMsgs.nativeElement,ballParent);
  }else{
    this.renderer.removeChild(ballChild,ball1Span);
    this.renderer.removeChild(ballChild,ball2Span);
    this.renderer.removeChild(ballChild,ball3Span);
    this.renderer.removeChild(ballParent,ballChild);
    this.renderer.removeClass(ballParent,"chat-bubble");
    this.renderer.removeClass(ballChild,"typing");
    this.renderer.removeClass(ball1Span,"dot"); 
    this.renderer.removeClass(ball2Span,"dot"); 
    this.renderer.removeClass(ball3Span,"dot"); 
    this.renderer.removeChild(this.divMsgs.nativeElement,ballParent);
  }
}

formatAMPM(date:any){
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return strTime;
}
  
}
