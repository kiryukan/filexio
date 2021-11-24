import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from '../model/user.model';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  user!:User;

  constructor(private authService: AuthService) { 
    this.getUser();
  }

  ngOnInit(): void {}

  public getUser(){
    this.user = this.authService.getUser();
  }

  onSubmit(data: NgForm){
    alert("ok");
  }

}
