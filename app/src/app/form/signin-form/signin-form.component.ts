import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';
import { Globals } from 'src/app/service/globals';

@Component({
  selector: 'app-signin-form',
  templateUrl: './signin-form.component.html',
  styleUrls: ['./signin-form.component.css']
})
export class SigninFormComponent implements OnInit {

  msg: string = "";
  closeResult: string = '';
  @Input()
  username: string = '';
  @Input()
  password: string = '';
  @Output() dataUsername: EventEmitter<any> = new EventEmitter();
  @Output() closeModal: EventEmitter<any> = new EventEmitter();
  isAuth = Globals.isAuth;
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(data: NgForm){
    console.log(data.value);
    this.authService.authenticate(data.value, (result:any) => {
      if(result){
        this.dataUsername.emit(result);
        this.router.navigate(['/dashboard']);
      }else{
        this.router.navigate(['/home']);
      }
    });/*.subscribe(
      (response:string)=>{
        console.log(response);
        Globals.isAuth = true;
        console.log(JSON.parse(JSON.stringify(response))["jwtToken"]);
        const token = JSON.parse(JSON.stringify(response))["jwtToken"];
        console.log(token);
        this.authService.setToken(token);
        this.authService.setAuthenticatedUser(data.value.username);
        this.dUsername.emit(data.value.username);
        //this.closeModalSignUp();
        this.router.navigateByUrl("/dashboard");
      },
      (error: Error)=>{
        console.log(error);
        this.msg = "Invalid credentials";
      });*/
  }

}
