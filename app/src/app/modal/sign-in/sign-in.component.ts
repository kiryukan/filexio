import { HttpErrorResponse } from '@angular/common/http';
import { Globals } from './../../service/globals';
import { User } from './../../model/user.model';
import { AuthService } from './../../service/auth.service';
import { NavbarComponent } from './../../navbar/navbar.component';
import { Component, OnInit, Input, ViewChild, ElementRef, EventEmitter, Output } from '@angular/core';
import { ModalDismissReasons, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  closeResult: string = '';
  @Input()
  username: string = '';
  @Input()
  password: string = '';
  @Output() dataUsername: EventEmitter<any> = new EventEmitter();
  @ViewChild('mymodal', { static: true }) input!: ElementRef; // retrieve child template var
  isAuth = Globals.isAuth;
  msg = Globals.msg;

  constructor(private modalService: NgbModal, private authService: AuthService, private router: Router) {}

  ngOnInit(){}

  openModalSignIn() {
    this.open(this.input);
  }

  closeModalSignUp() {
    this.modalService.dismissAll("dismissed");
  }

  open(content:any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }


  onSubmit(data: NgForm){
    console.log(data.value);
    this.dataUsername.emit(data.value.username);
    this.authService.authenticate(data.value, (result:any) => {
  6
      this.closeModalSignUp();
      this.router.navigateByUrl("/dashboard");
    });
    /*.subscribe(
      (response:string)=>{
        console.log(response);
        Globals.isAuth = true;
        console.log(JSON.parse(JSON.stringify(response))["jwtToken"]);
        const token = JSON.parse(JSON.stringify(response))["jwtToken"];
        console.log(token);
        this.authService.setToken(token);
        this.authService.setUser(data.value.username, token);
        this.dataUsername.emit(data.value.username);
        this.closeModalSignUp();
        this.router.navigateByUrl("/dashboard");
      },
      (error: Error)=>{
        console.log(error);
        this.msg = "Invalid credentials";
      });*/
  }

}
