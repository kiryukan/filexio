import { HttpClient, HttpEvent, HttpEventType, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { File } from '../model/file.model';
import { UserComponent } from '../user/user.component';
import { UserService } from './user.service';
import { AuthService } from './auth.service';
import { FileType } from '../model/filetype.model';
import { forEach } from 'node_modules/@angular-devkit/schematics/src';
import { Router, RouterLink } from '@angular/router';
import { SharingService } from './sharing.service';
import { environment } from 'src/environments/environment';
import * as FileSaver from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  SERVER_URL: string = 'http://localhost:3009';
  user!: User;
  files: File[] = [];
  rootFolder: any;
  completePath: string[] = [];
  completePathForDatas: string[] = [];
  actualFolder!: File;
  fileTypes: FileType[] = [];
  filesTohandle: File [] = [];
  folderIcon: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAABIklEQVRoge3asU0DMRjF8c8IBYkJ2IImDWEGCrbJAhBSUTEAzMIEVAzAHYwQ0fwp4oCTOyQbLvg79H5V5NjSe5EvRb6YiUgWYAIsgZY8LXBZO3cHcJNZIPUOTGtn3wI0Mdwsc/9t3P8MHO87X7bNR1yw/wh4isfu9pmtSGmReOYUWP3gSv5WAyyAySBF4rkLvq7lX7serEgNwHmM2/S9OZoiZt28BzXDDOlwdwF4NbOTCllKvO0uhM2LMV2rVAghbC0k3wRnlTJlA2bfPtN62J1QEW9UxBsV8UZFvFERb1TEGxXxRkW8URFvVMQbFfFGRbz5l0Vas/UQpVKWbEnGz0FPOlZ4MLO5mT2O6Cfg+84K6z8MLKg3DyzxAlzRNwwVkV4fqgTDgpBHIFgAAAAASUVORK5CYII=";
  fileIcon: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAAA4klEQVRoge3ZwQnCQBCF4RmxBhE70DLs3B6ExQ7s4nmZgyCG7DKzecr7L8Zs2M2XXEJiprjypUEAKFy7mdnV3Z8Zk+0yJhnsbGY3AKfylRBVzRs9yjETIO3t95i9zseCVfMCOAC4l9+Zakhs12NmQOJ/LWYWJPbVYWZCYn8NZjYkxvIxW0BiPBezFSSO6cKsetZy98Xjehu8OM3dL98GfwmyeB778dMZr/fCrIFv+fSbmiBsCcKWIGwJwpYgbAnCliBsCcKWIGwJwpYgbAnCliBsCcLWqrfxFR97svubO6LYegEH5GrAFMUJsQAAAABJRU5ErkJggg==";
  musicIcon: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAAC5ElEQVRoge2YzatVZRTGfysUJD/Lr0QoQ7IGdyAIkoIDcRLoRHDguCAaWf9CGIT/gCANmtXgRqk4CPEDPzBJQRG/QC83b0nSGakhGFd/Ds46eKRzzzmevfdtJ/uZ7L3Xu87zrGe/77v32gca1AvRb1C1It0jwK6IeFwW4WtlEb0kdgA/qfNmRc1EFZyJk+qCMvn7ilbBqd7L4wl1fpkaM4pWwam+r/6e52fVRWXq9BStilNdo05k6KK6tEytnqJVcapvq7cyfEldVqZeT9GqONW31Gs5dF1dVabmrBnJ+Er1Sg7fVFdXLloVp/qmeiFTJtV3KxetilNdop7PtDvq2spFq+JUF6vnMnVKfa9y0ao41fnq8Uy/p471yx+qaYyIvnkvgwI35q+IWDnT4P/JSN865oxKOipGuSnDmP+v2vjS0RipGxojdUOhp5b6BbAZOBARxzN2AHgD+Dwi/lS/BNbkTyaBbyLiru2vwm2Zey4ibhWpZVChg/qinzNlf16v8Dk2ZKylPlGvqk/V37IF+aor9x91+6h1QPGl1fmi25zHTV1jS7rOH0TEGLAfeAf4CPg2zz8F5gIfFymk6AtxKdACxtSFtI20gOW8aKSDyTzOi4gJAHUqY1M98svBEEvrvjqeaVvVo+oPef1J5rTUh+pO9bb6uNOaq4fVafVXdfGodUCBpaXOBRYCl4H7wEZgPXAmU7pnZAHwI7AM+KwzG8BV2rO0Adg9ai3DFDvjnbD9na26Rz2m/pKben3G92ZeS32gfqD+aynnxn+k/jFKHR0U2eydjf4QOA18CFwB7mS8e0aeRMTNiJjuKq7TPAYgAzrxQSiy2TtG/qZtBOAUbWPQe7N346DtP+XWAa8DXxeopZCRR8A4MAHcAPYB4xExrX5He3YADtN75s8DW4CzwCHg+wK19Mcwa3M2MBsvxNqgMVI3NEbqhsZI3dAYqRsaI3XDK2NkqO63Do3jILwyM9KgbngGFknacqyldx4AAAAASUVORK5CYII=";
  videoIcon: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAABNklEQVRoge2ZsWrDMBCGTyGh7xACeY9CIdAlb9Mtc56lY9cMHTs0eYCWTn2MtJ2/LDYoITKhPlvScd9i+wR393M6y5ZEHMfpIoiIAAcRuc+cy3/ZhxAeWiHkzqYPIYQwvTS096242FaaPS7ApEtpTZgRYqZHzFQk2ew14M1eMi6kNFxIaagLAV6BT+BR2/ctwdFa3TlnByw1/HbFShsUnAO/zfUH2AAzDf/XYqUNCs6BOfAcPX8Da40Yl7HSBkXnwAr4GmK6jSqksc2AJ+DYDP8BW+BONdbQQqKxBfASVWevFSv3OqL7H+RT6wbn1Tc7Vl6/GFoQVadRV6y0oZ/zN+CDET4aBxUyJnHeudcRNVxIabiQ0jAj5Gzvt8ZXcIvNivjRWwGYEWLu6K3XJkBm3nMn4Dg1cAL6+m3BcEHPpwAAAABJRU5ErkJggg==";

  constructor(
    private http: HttpClient, 
    private router: Router, 
    private authService: AuthService,
    private sharingService: SharingService){
  }

  initFiles(next: Function){
    this.user = this.authService.getUser();
    console.log(this.user);
    /*this.files = [
      new File(1, 'root', 0, '', '', new Date("15/02/2021"), 'folder', this.folderIcon, '', this.user, true, 0, [2, 3]),
      new File(2, 'Feel_good.mp3', 30000, 'root/', 'mp3', new Date("2021/5/21"), 'music', this.musicIcon, '', this.user, false, 1, []),
      new File(3, 'vidéos', 0, 'root/', '', new Date("12/03/2021"), 'folder', this.folderIcon, '', this.user, false, 1, [4]),
      new File(4, 'La_revanche_du_pain_perdu.mp4', 30000, 'root/vidéos/', 'mp4', new Date("14/03/2021"), 'mpeg4 video file', this.videoIcon, '', this.user, false, 3, []),
      new File(5, 'séries', 0, 'root/vidéos', '', new Date("12/03/2021"), 'folder', this.folderIcon, '', this.user, false, 3, []),
    ]*/
    this.getAllFiles((value: any) => {
      if(value === true){
        this.getAllFileTypes((valueFt: any) => {
          if(valueFt === true){
            if(this.actualFolder === undefined){
              this.setCurrentFolder(this.getRootFolder());
            }
            next(valueFt);
          }
        });
      }
    });
    //this.setCurrentFolder();
  }

  getRootFolder(){
    return this.files.find(f => {return f.isRoot === true})!;
  }

  /*getAllFiles(){
    return this.files;
  }*/

  getAllFiles(next: Function){
    let httpOptions = this.sharingService.getHttpOptions();
    this.http.get<File[]>(`${environment.baseUrl}/api/user/`+ this.user.id +'/files', httpOptions).subscribe((files)=>{
      this.files = files;
      //this.setCurrentFolder(this.getRootFolder());
      //if(this.actualFolder === undefined){this.setCurrentFolder(this.getRootFolder());}
      console.log(this.actualFolder);
      console.log(this.files);
      //return this.files;
      next(true);
    });
  }

  getAllFileTypes(next: Function){
    let httpOptions = this.sharingService.getHttpOptions();
    this.http.get<FileType[]>(`${environment.baseUrl}/api/filetype/get/all`, httpOptions).subscribe((fileTypes)=>{
      this.fileTypes = fileTypes;
      console.log(this.fileTypes);
      //return this.files;
      next(true);
    });  
  }

  getFolderById(id: number){
    return this.files.find(file => { return file.id === id });
  }

  getFileById(id: number){
    return this.files.find(file => { return file.id === id });
  }

  getFolderByName(name: string){
    return this.files.find(file => { return file.name === name });
  }

  getFilesInFolder(id: number){
    let filesInFolder: File[] = [];
    this.files.forEach(file => {
      if(file.parentFolder !== null && file.parentFolder.id === id){ filesInFolder.push(file); }
    });
    this.constructPath(id);
    console.log(filesInFolder);
    return filesInFolder;
  }

  constructPath(idCurrentFolder: number){
    let currentFolder = this.files.find((file)=>{ return file.id === idCurrentFolder; });
    let path: string[] = [];
    let pathForDatas: string[] = [];
    while(currentFolder !== undefined && currentFolder.isRoot !== true){
        pathForDatas.push(currentFolder.name + "/");
        path.push(currentFolder.name);
        let parentId = currentFolder.parentFolder.id;
        currentFolder = this.files.find((file) => { return file.id === parentId}) as File;
        //path.push(currentFolder.name + "/");
    }
    if(currentFolder !== undefined && currentFolder.isRoot){
      pathForDatas.push(currentFolder.name + "/");
      //path.push("root/");
      path.push("root");
    }
    console.log(...path);
    this.completePath = path.reverse();
    this.completePathForDatas = pathForDatas.reverse();
  }

  /*constructPath(idCurrentFolder: number){
    let currentFolder = this.files.find((file)=>{ return file.id === idCurrentFolder; });
    let path: string[] = [];
    while(currentFolder !== undefined && currentFolder.isRoot !== true){
        path.push(currentFolder.name + "/");
        let parentId = currentFolder.parentFolder.id;
        currentFolder = this.files.find((file) => { return file.id === parentId}) as File;
        //path.push(currentFolder.name + "/");
    }
    if(currentFolder !== undefined && currentFolder.isRoot){
      //path.push(currentFolder.name + "/");
      //path.push("root/");
      path.push(currentFolder.name + "/");
    }
    console.log(...path);
    this.completePath = path.reverse();
  }*/

  getCompletePath(){
    return this.completePath;
  }

  getCompletePathToString(backend: boolean){
    let path = "";
    let pathForDatas = "";
    if(backend){
      console.log("complete path befor string: " + this.completePathForDatas);
      for(let p of this.completePathForDatas){
        pathForDatas += p;
      }
        console.log("path to string: " + pathForDatas);
        return pathForDatas;
    }
    else{
    console.log("complete path befor string: " + this.completePath);
    for(let p of this.completePath){
      path += p;
    }
      console.log("path to string: " + path);
      return path;
    }
  } 

  getParentFolder(currentFolderId: number){
    let current = this.getFolderById(currentFolderId);
    return current;
  }

  setCurrentFolder(file: File){
    this.actualFolder = file;
  }

  getActualFolder(){
    return this.actualFolder;
  }

  reloadDatas(){
    
  }
  /*setCurrentFolder(){
    this.actualFolder = this.files.find((file) => { return file.isRoot === true })!;
    console.log(this.actualFolder);
  }*/

  createFolder(name: string, next: Function){
    this.createFile(name, 1, 'none', 'folder', (result:boolean) => {
      next(result);
    })
  }

  /*createFileData(name: string, next: Function){
    this.createFile(name, 1, 'none', 'folder', (result:boolean) => {
      next(result);
    })
  }*/

  createFile(name:string, fileTypeId: number, ext:string, type:string, next:Function){
    console.log(this.user);
    console.log(this.actualFolder.name);
    this.constructPath(this.actualFolder.id);
    let path = this.completePathForDatas.join('');
    let user = this.authService.getUser();
    
    user.id = this.authService.getUser().id;

    let fileType: FileType = { 
      id: fileTypeId,
      type: type,
      ext: ext,
      iconThin: 'none',
      iconFilled: 'none',
      iconSvg: 'none*'
    };

    let file = new File(
      length,
      name, 
      0, 
      path, 
      "", 
      new Date(Date.now()), 
      fileType, 
      "none", 
      user,
      false, 
      this.actualFolder, 
      []
      )

      this.recordNewFile(file, 'folder').subscribe((recordedFile) => {
        if(recordedFile){
          this.files.push(recordedFile);
          console.log(this.files);
          next(true);
        }
      });
  }

  recordNewFile(file: File, ext: string){
    file.user = this.authService.getUser();
    console.log(file);
    let httpOptions = this.sharingService.getHttpOptions();
    return this.http.post<File>(`${environment.baseUrl}/api/file/add`,file,httpOptions);          
  }

  uploadFile(formData: FormData): Observable<any>{
    if(this.actualFolder.isRoot === false){
      formData.append('path', this.getCompletePathToString(true));
      //console.log("path name: " + formData.getAll('path'));
    }
    else
    {
      formData.append('path', this.actualFolder.name);
      //console.log("path name: " + formData.getAll('path'));
    }

    formData.append('userId', this.user.id.toString());
    //console.log("upload userId: " + this.user.id.toString());
    formData.append('parentId', this.actualFolder.id.toString());
    //console.log("upload parent folder: " + this.actualFolder.id.toString());
  
    let token = this.authService.getToken();
    return this.http.post(`${environment.baseUrl}/api/upload/file`, formData, {
      reportProgress: true,
      observe: 'events',
      headers: new HttpHeaders({
          Authorization: 'Bearer '+token,
      })
    })
  }

  deleteFile(id: number, next: Function){
    for(let i=0; i<this.files.length; i++){
      if(this.files[i].id === id){
        this.files.splice(i, 1)
      }
    }
    let httpOptions = this.sharingService.getHttpOptions();
    this.http.delete<File>(`${environment.baseUrl}/api/file/delete/`+id, httpOptions).subscribe((file:any)=>{
      next(true);
    });
  }

  downloadFile(fileId: number, next: Function){
    //let httpOptions = this.sharingService.getDownloadHttpOptions;
    let token = this.authService.getToken();
    this.http.get(`${environment.baseUrl}/api/file/download/`+fileId, {
      responseType: 'blob',
      headers: new HttpHeaders({
          Authorization: 'Bearer '+token,
      })
    }).subscribe((blob:any)=>{
      FileSaver.saveAs(blob, "test");
      next(true);
    });
  }

  playFile(id: number){

  }

  copyFile(files: File[], next: Function){
    let httpOptions = this.sharingService.getHttpOptions();
    let folderId = this.actualFolder.id;
    let filesIdToCopy: number[] = [];
    for(let file of files){
      filesIdToCopy.push(file.id);
    }
    this.http.post<File[]>(`${environment.baseUrl}/api/file/copy/`+folderId, filesIdToCopy, httpOptions).subscribe((file:any)=>{
      next(true);
    });
  }

  moveFile(files: File[], next: Function){
    let httpOptions = this.sharingService.getHttpOptions();
    let folderId = this.actualFolder.id;
    let filesIdToCopy: number[] = [];
    for(let file of files){
      filesIdToCopy.push(file.id);
    }
    this.http.post<File[]>(`${environment.baseUrl}/api/file/move/`+folderId, filesIdToCopy, httpOptions).subscribe((file:any)=>{
      next(true);
    });
  }

  updateFile(file: File, next: Function){
    let httpOptions = this.sharingService.getHttpOptions();
    let fileId = file.id;
    console.log("renamed file: "+file.id + " - " + file.name);
    this.http.post<File>(`${environment.baseUrl}/api/file/rename`, file, httpOptions).subscribe((file:any)=>{
      next(true);
    });
  }

}
