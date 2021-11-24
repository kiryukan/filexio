import { HttpHeaders } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Credentials } from 'src/app/model/credentials.model';
import { User } from 'src/app/model/user.model';
import { AuthService } from 'src/app/service/auth.service';
import { SharingService } from 'src/app/service/sharing.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-modify-password',
  templateUrl: './modify-password.component.html',
  styleUrls: ['./modify-password.component.css']
})
export class ModifyPasswordComponent implements OnInit {

  user!:User;
  @Input() oldPassword!: string;
  @Input() newPassword!: string;
  @Input() newConfirmPassword!: string;
  @Output() modalMsg: EventEmitter<any> = new EventEmitter();
  constructor(private authService: AuthService, private sharingService: SharingService, private userService: UserService, private modalService: NgbModal,) { }

  ngOnInit(): void {}

  onSubmit(data: NgForm){
    this.saveNewPassword(data.value.oldPassword, data.value.newPassword, data.value.newConfirmPassword, ()=>{

    });
  }

  saveNewPassword(oldPassword:string, newPassword:string, newConfirmPassword:string, next:Function){
    console.log("newPass: "+newPassword + "Confirm: "+newConfirmPassword);
    if(newPassword === newConfirmPassword){
      this.checkUserPassword(oldPassword, (passwordOk:boolean) => {
        if(passwordOk){
          this.updateUserPassword(newPassword, (success:boolean) => {
            success ?
              this.modalMsg.emit("New password recorded successfully") :
              this.modalMsg.emit("There was a problem with new password record");
          });
        }
        else{ console.log("bad password entered..."); }
      });
    }
    else{ console.log("passwords are not equals..."); }
  }

  checkUserPassword(oldPassword: string, next:Function){
    this.user = this.authService.user;
    console.log("username: "+ this.user.username);
    let userName = this.user.username;
    let credentials: Credentials = {
      username: userName,
      password: oldPassword
    };

    this.authService.findUserByNameAndPassword(credentials, (value:any) => {
      if(value && value.username === this.user.username){ 
        next(true);
      }
      else{ 
        next(false); 
      }
    });
  }

  updateUserPassword(newPassword: string, next: Function){
    let user = this.user;
    user.password = newPassword;
    let httpOptions = this.sharingService.getHttpOptions();
    /*const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer '+ this.authService.getToken()
      })
    };*/
    this.userService.updateUser(user, httpOptions).subscribe((value: any) => {
      if(value !== undefined){
        next(true);
      }
      else{
        next(false);
      }
    });
  }
}
