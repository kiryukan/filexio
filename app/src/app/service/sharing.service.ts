import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { FileType } from '../model/filetype.model';
import { AuthService } from './auth.service';

@Injectable()
export class SharingService {

    constructor(private authService: AuthService){}

    getHttpOptions(){
        let token = this.authService.getToken();
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/json',
                Authorization: 'Bearer '+token
            })
        };
        return httpOptions;
    }

    getUploadHttpOptions(){
        let token = this.authService.getToken();
        return {
            reportProgress: true,
            observe: 'events',
            headers: new HttpHeaders({
                Authorization: 'Bearer '+token,
            })
        };
    }

    getDownloadHttpOptions(){
        let token = this.authService.getToken();
        const httpOptions = {
            headers: new HttpHeaders({
                'responseType':  'blob',
                Authorization: 'Bearer '+token
            })
        };
        return httpOptions;
    }
}