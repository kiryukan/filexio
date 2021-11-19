import { UserService } from './../service/user.service';
import { Component, OnInit } from '@angular/core';
import { User } from '../model/user.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  public users: User[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getAllUsers();
  }

  public getAllUsers(): void{
    this.userService.getAllUsers().subscribe((res: User[]) => {
      this.users = res;
    },
    (error: HttpErrorResponse) => {
      console.log(error.message);
    });
  }
}
