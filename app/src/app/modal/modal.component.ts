import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal } from 'node_modules/@ng-bootstrap/ng-bootstrap';
import { SigninFormComponent } from '../form/signin-form/signin-form.component';
import { SignupFormComponent } from '../form/signup-form/signup-form.component';
import { AuthService } from '../service/auth.service';
import { SignInComponent } from './sign-in/sign-in.component';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {

  isToSign:   boolean = false;
  isToCreate: boolean = false;
  isAuth:     boolean = false;
  toCreateFolder: boolean = false;
  closeResult: string = '';
  modalName: any;
  @Input() closeModal: any;
  @Input() dataUsername: any;
  @Output() dUsername: EventEmitter<any> = new EventEmitter();
  @Input() reloadData: any;
  @Output() reload: EventEmitter<any> = new EventEmitter();
  @ViewChild('mymodal', { static: true }) input!: ElementRef;
  @ViewChild('signin', { static: true }) signinForm!: SigninFormComponent;
  @ViewChild('signup', { static: true }) signupForm!: SignupFormComponent;


  constructor(private modalService: NgbModal) { }

  ngOnInit(): void {
  }

  openModalSignInForm(){
    this.modalName = "Sign in";
    this.isToSign = true;
    this.isToCreate = false;
    this.toCreateFolder = false;
    this.open(this.input);
  }

  openModalSignUpForm(){
    this.modalName = "Create new account";
    this.isToSign = false;
    this.isToCreate = true;
    this.toCreateFolder = false;
    this.open(this.input);
  }

  openCreateFolderForm(){
    this.modalName = "Create new folder";
    this.isToSign = false;
    this.isToCreate = false;
    this.toCreateFolder = true;
    this.open(this.input);
  }
  
  emitUsername(data: any){
    this.isAuth = true;
    console.log(data);
    this.dataUsername = data;
    this.dUsername.emit(data);
    this.closeModalSignUp();
  }

  emitClose(data: any){
    this.closeModalSignUp();
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

  emitReloadData(data: any){
    this.reload.emit(data);
  }

  /*onSubmit(){
  }*/
}
