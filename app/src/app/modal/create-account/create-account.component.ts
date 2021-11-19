import { HttpErrorResponse } from '@angular/common/http';
import { Globals } from './../../service/globals';
import { User } from './../../model/user.model';
import { AuthService } from './../../service/auth.service';
import { NavbarComponent } from './../../navbar/navbar.component';
import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { ModalDismissReasons, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit {

  closeResult: string = '';
  @Input()
  username: string = '';
  @Input()
  password: string = '';
  @ViewChild('createModal', { static: true }) input!: ElementRef; // retrieve child template var
  isAuth = Globals.isAuth;
  @Output() dataUsername: EventEmitter<any> = new EventEmitter();
  msg = Globals.msg;
  successfulCreated: Boolean = false;

  constructor(private modalService: NgbModal, private authService: AuthService, private userService: UserService) {}

  ngOnInit(){}

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
          this.msg = "Welcome " + user.username;
          this.dataUsername.emit(data.value.username);
          this.closeModalSignUp();
        },
        (error: HttpErrorResponse)=>{
          console.log(error.error['error']);
          this.msg = "A user with this " + error.error['error'] + " already exist";
        });
      }
      else{
        this.msg = "Please, complete all fields";
      }
  }

  isSuccessfullyCreated(success: boolean){
    this.successfulCreated = success;
  }

}
