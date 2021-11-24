import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { User } from 'src/app/model/user.model';
import { AuthService } from 'src/app/service/auth.service';
import { Globals } from 'src/app/service/globals';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrls: ['./signup-form.component.css']
})
export class SignupFormComponent implements OnInit {

  closeResult: string = '';
  @Input()
  username: string = '';
  @Input()
  password: string = '';
  @ViewChild('createModal', { static: true }) input!: ElementRef; // retrieve child template var
  isAuth = Globals.isAuth;
  @Output() dataUsername: EventEmitter<any> = new EventEmitter();
  @Output() modalMsg: EventEmitter<any> = new EventEmitter();
  msg = Globals.msg;
  successfullyCreated: Boolean = false;

  constructor(
    private modalService: NgbModal,
    private authService: AuthService,
    private userService: UserService,
    private router: Router) { }

  ngOnInit(): void {
  }

  openModalSignUp() {
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
    if(data.value.username !== "" && data.value.email != "" && data.value.password != ""){
      this.userService.addUser(data.value).subscribe(
        (user:User)=>{
          console.log(user);
          this.dataUsername.emit(data.value.username);
          this.closeModalSignUp();
          this.authService.authenticate(data.value, (result:any) => {
            if(result){
              this.dataUsername.emit(result);
              this.router.navigate(['/dashboard']);
            }else{
              this.router.navigate(['/home']);
            }
          });          
          console.log("after user");
        },
        (error: HttpErrorResponse)=>{
          console.log(error.error['error']);
          this.msg = "A user with this " + error.error['error'] + " already exist";
        });
      }
      else{
        this.msg = "Please, complete all fields";
        this.modalMsg.emit("Please, complete all fields");
      }
  }

  isSuccessfullyCreated(success: boolean){
    this.successfullyCreated = success;
  }



}
