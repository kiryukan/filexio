import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpResponse } from 'node_modules/@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public getAllUsers():Observable<User[]>{
    return this.http.get<User[]>(`${environment.baseUrl}/api/user/all`);
  }

  public getUser(userId: number):Observable<User>{
    return this.http.get<User>(`${environment.baseUrl}/api/user/get/`+userId);
  }

  public getUserByUsername(httpOptions: any):Observable<any>{
    return this.http.get<User>(`${environment.baseUrl}/api/user/get/username`, httpOptions);
  }

  public getUserByEmail(email: string, httpOptions: any):Observable<any>{
    return this.http.get<User>(`${environment.baseUrl}/api/user/get/by-email/`+email, httpOptions);
  }

  public addUser(user: User):Observable<User>{
    return this.http.post<User>(`${environment.baseUrl}/api/user/add`, user);
  }

  public updateUser(user: User, httpOptions: any):Observable<any>{
    return this.http.put<any>(`${environment.baseUrl}/api/user/update`, user, httpOptions);
  }
}
