import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { ModalDismissReasons, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals } from '../service/globals';
import {Router, NavigationEnd,ActivatedRoute} from '@angular/router';
import { SharingService } from '../service/sharing.service';
import { AuthService } from '../service/auth.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit{

  closeResult: string = '';

  isAuth    = Globals.isAuth;
  username  = Globals.user;
  actualRoute = "";
  @Input() dUsername!: string;

  constructor(private router: Router, private authService: AuthService) { 
  }

  ngOnInit(): void {}

  emitUsername(data: any){
    this.isAuth = this.authService.isAuth();
    console.log(this.dUsername);
    this.dUsername = data;
  }

  disconnect(){
    //Globals.emptySession();
    this.authService.disconnect();
    this.isAuth = this.authService.isAuth();
    this.router.navigateByUrl("/home");
  }

  getUrl(){
    return this.router.url;
  }

}


