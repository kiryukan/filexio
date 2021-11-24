import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { FileType } from '../model/filetype.model';
import { SharingService } from './sharing.service';

@Injectable({
  providedIn: 'root'
})
export class FileTypeService {
  fileTypes: FileType[] = [];

  constructor( private http: HttpClient, private sharingService: SharingService ) {}

  getAllFileTypes(){
    const httpOptions = this.sharingService.getHttpOptions();
    return this.http.get<FileType[]>(`${environment.baseUrl}/api/filetype/get/all`,httpOptions);
  }
}
