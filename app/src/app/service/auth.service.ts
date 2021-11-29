import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JsonObject } from 'node_modules/type-fest';
import { nextTick } from 'process';
import { Observable } from 'rxjs';
import { Credentials } from '../model/credentials.model';
import { Role } from '../model/role.model';
import { User } from '../model/user.model';
import { Globals } from './globals';
import { SharingService } from './sharing.service';
import { UserService } from './user.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user!: User;
  isAuthentified: boolean = false;

  constructor(private http: HttpClient, private userService: UserService) { }

  public authenticate(credential: Credentials, callback: Function){
    this.http.post<string>(`${environment.baseUrl}/authenticate`, credential).subscribe(
      (tokenResp) => {
        const token = JSON.parse(JSON.stringify(tokenResp))["jwtToken"];
        console.log(token);
        this.setToken(token);
        const httpOptions = {
          'headers': new HttpHeaders().set(
            'Content-Type',  'application/json').set(
            'Authorization', 'Bearer '+token)
        };
        this.userService.getUserByUsername(httpOptions).subscribe(
          (user: any) => {
            console.log(user);
            const parsedUser = JSON.parse(JSON.stringify(user));
            this.setAuthenticatedUser(parsedUser);
            callback(user.username);
          },
          (error: Error) => {
            callback(false);
          }
        );
      }
    )
  }

  public getAuthentifiedUserByEmail(email: string, token: string){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer '+token
      })
    };
    this.userService.getUserByEmail(email, httpOptions)
      .subscribe((u:User) => {
        const user = JSON.parse(JSON.stringify(u));
        this.setAuthenticatedUser(user);
    })
  }

  // auth : get user trough all application
  public setUser(email: string, token: string){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer '+token
      })
    };
    this.userService.getUserByEmail(email, httpOptions)
      .subscribe((user:User) => {

        this.setAuthenticatedUser(user);
    })
  }

  setAuthenticatedUser(user:any){
    const userData = {
      id: (user as any).id,
      username: (user as any).username,
      email: (user as any).email,
      password: '',
      rootFolderName: '',
      role: (user as any).role,
      lastConnection: '',
      usedDiskSpace: 0
    }
    this.user = userData;
    this.isAuthentified = true;
  }

  getUser(){
    return this.user;
  }

  /*public setGlobalUser(email: string, token: string){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer '+token
      })
    };
    this.http.get<User>('http://localhost:3009/user/get/by-email/'+email,httpOptions)
      .subscribe((user:User) => {
      if(Globals.user === ""){
        Globals.userId = user.id;
        Globals.user = user.username;
        Globals.role = user.role.name;
        Globals.msg = "Welcome "+user.username;
      }
    })
  }*/

  public findUserByNameAndPassword(credential: Credentials, next: Function){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer '+ this.getToken()
      })
    };
    this.http.post<User>(`${environment.baseUrl}/api/user/check-password`, credential, httpOptions).subscribe(
      (user) => {
        next(user);
      });
  }

  public isAuth(){
    return this.isAuthentified;
  }

  public disconnect(){
    this.isAuthentified = false;
    let role: Role = {id:0, nom:'', description:''};

    const userData = {
      id: 0 as any,
      username: '' as any,
      email: '' as any,
      password: '',
      rootFolderName: '',
      role: role as any,
      lastConnection: '',
      usedDiskSpace: 0
    }
    this.user = userData;
  }

  // auth
  public getToken(){
    return localStorage.getItem("auth-token");
  }

  // auth
  public setToken(token: string){
    localStorage.setItem("auth-token",token);
  }
}
