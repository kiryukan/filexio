import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { FileService } from '../service/file.service';
import { File } from '../model/file.model';
import { HttpClient, HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-dashboard-twice',
  templateUrl: './dashboard-twice.component.html',
  styleUrls: ['./dashboard-twice.component.css']
})
export class DashboardTwiceComponent implements OnInit {

  files:File[] = [];
  actualFolder!: File;
  actualPath!: string[];
  path: string[] = [];
  arrow: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAACUElEQVRoge2ZPWsUQRyHf3O+FaaRaCFCtNOUNhYqGiwshIBgRHz5ADaCn0BsLBVB/BYxoo02CSRnEggEFYtUak6xESvtkriPxW4wzO3ezt6yNzPhHthijvsvv4e7eZccACaBb9lz2aUmOIApYJP/rPvOVJkcifhECiQ2gUnf2ZwBrgEblsQWcMt3Nmd6SNz2nc2Z3S5xx3c2Z3aDhMlGoRlJez3m+CXps6Q1SYuS3hhjflR5gQE6ksYaCFeHRFJb0jNJL40xSVlBSxJNp+qDlqSLkqYlLQOnXQruSdpqOFgdzkhaAR4D+3p+s2AWH8gECBwETgETwANglWLeAiP9ytxsWiYny0lgGkhyZFaAw2UvKJK5MSAHO89Z4GuOzCywp6w4qNkdGAXmcmQeuRQXrXyvDyB7Xp79wLyVJwEmXIqDWsYDR4AvVp4l1+KgNlakfcYeAK64Fge11QVeWCLvqhRvHz508Hz4AIzn9JUTPjP1DfDekrnb8h2qT15Z7fOxiixY7fFYRey9ypjxEqMmpAvHPzs+2oj1F+nKHavIUav9O1aRY1a7E6vIBau9FqvIVavd9pKiDgVLlOO+c1UGmLFE7MkxfIBzdDPlO1clSDdW65bERyCefk661V3I6RvlW91QoPjw4anvbM5kfcL+OwG0gQO+85WSDbH26LTNB2DUd8YugBHSI9NLwEO6d387mQcOFb0IgLrthvkLPKHHIbbPyx1XViXdN8b0PC0JVSSRNCvpuaTXxpjSO5xQRL5nzydJy0qv3n76jTRkyJAg+Qf+0H4L5Z7FZgAAAABJRU5ErkJggg==";
  createFolderIcon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAABmJLR0QA/wD/AP+gvaeTAAACe0lEQVRoge2Zv2/TQBTH3zVpKgHd2dhJEQsL7YZoO1RQkMLC31C1BHYmVGBiYoIRZpK/A1gQpVEEXVAKMyoSFMiHwc+16zjuOT98V8kfyUp87z37fX3vTj6fSElJiRVADXgK7GPHPnDbdd4DAE8sBcQ5BK64zv0YQE+TW7T0f6b+HeDMtPOzJnzEOfzngA8a9nyaueUirxCNuQz8GqEkx6UHPAZqExGicTeIyrJoticmxAXAkqbbSzOeGiEig/nOuExmklSTDcA3ETnvIJc8fE82mPDPaSqrOMYYc6whNhNcdZSTNcDi0DFdDnZPKIX4RinEN0ohvlEK8Y1SiG+UQnzDKyHALNAAXgG7wAHwE/gCvAbuAAOLweRFnL7GA+tA1+LLyR6w5t16BJgh+EYV8hG4B9SBs3rUgSawk1Tlk5BQxG9gA6hk+FaATYJvzv4I0XLqq4jrKfY20EppX46JWUsaCxVCMLDDMbExxGdoTsBWbMxUrYKmAcHsFI6J1HI6QUgV+KQuDZfTb7hJ9MIY8y9vsDHmr4i81NP1I4ODHunoLS9m+GTmBCyoy2froEkD/NBbzut5G3taGjOv5wcuSyv50PI8xKRvP7IU3yO7est6ho9taXVc9sg7/V0Z4xqr+vv2qIVo12lpjAtbA9zS++0w+vQb9mojboi/7xTN5ghCmmruArNxQ41AjIv9wENgOSXZFvAmpX1FY/rAzTShhQM8jInZImO9QVBOTaL3rMHNUJcAj/TpQjBm7hPMSOf0WAAeEL2S9IFtwKuFoYiIANcIVoIn0U0rJ5N2UVcQzF6rInJXRC6JyAURqYjIVxF5LyJtEWkbY/4kY/8DomXiM78egDYAAAAASUVORK5CYII=";
  rightArrow: string = `<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" width="15" height="15" viewBox="0 0 256 256" xml:space="preserve">
  <desc>Created with Fabric.js 1.7.22</desc>
  <defs>
  </defs>
  <g transform="translate(128 128) scale(0.72 0.72)" style="">
    <g style="stroke: none; stroke-width: 0; stroke-dasharray: none; stroke-linecap: butt; stroke-linejoin: miter; stroke-miterlimit: 10; fill: none; fill-rule: nonzero; opacity: 1;" transform="translate(-175.05 -175.05) scale(3.89 3.89)" >
    <polygon points="24.64,90 20.36,85.72 61.08,45 20.36,4.28 24.64,0 69.64,45 " style="stroke: none; stroke-width: 1; stroke-dasharray: none; stroke-linecap: butt; stroke-linejoin: miter; stroke-miterlimit: 10; fill: rgb(255,255,255); fill-rule: nonzero; opacity: 1;" transform="  matrix(1 0 0 1 0 0) "/>
  </g>
  </g>
  </svg>`;

  // upload states var
  selectedFile: string = "";
  form: FormGroup;
  progress: number = 0;

  constructor(private http: HttpClient, private router: Router, private fileService: FileService,  public fb: FormBuilder, private sanitizer: DomSanitizer) {
    this.form = this.fb.group({
      file: [null]
    })
  }

  ngOnInit(): void {
    this.fileService.initFiles((value:any) => {
      if(value === true){
        //this.setCurrentFolder(); // ### - Share to global fileService
        this.actualFolder = this.fileService.getActualFolder();
        this.files = this.fileService.getFilesInFolder(this.actualFolder.id);
        this.path = this.fileService.getCompletePath();
        console.log(this.actualFolder);
        console.log(this.files);
      }
    });
  }

  getSelectedFolder(id: number){
      this.actualFolder = this.fileService.getFolderById(id) as File;
      if(this.actualFolder.id === id){
        console.log("current folder well assigned");
        this.fileService.actualFolder = this.actualFolder;
        console.log("actual entered folder: "+this.fileService.actualFolder.name);
        this.files = this.fileService.getFilesInFolder(id);
        this.fileService.constructPath(id);
        this.actualPath = this.fileService.completePath;
        console.log("actual path: " + this.actualPath);
        this.path = this.fileService.getCompletePath();
      }
  }

  sanitize(value: any){
    return this.sanitizer.bypassSecurityTrustHtml(value);
  }

  directAccessFolder(id: number){
    // ### - retrieve selected uri from it's dom index (path[index])
    let folderName = this.path[id];
    // ### - remove slash from uri to get name ("root/" --> "root")
    //folderName = folderName.slice(0, -1);
    console.log(folderName);
    if(folderName === 'root'){
      let folder = this.fileService.getRootFolder();
      folderName = folder.name;
    }
    // ### - retrieve folder File object from it's name
    this.actualFolder = this.fileService.getFolderByName(folderName) as File;
    // ### - retrieve all details from selected folder
    this.getSelectedFolder(this.actualFolder.id);
  }

  /*getActualFolder(){
    return this.actualFolder
  }*/

  getUrl(){
    return this.router.url;
  }

  backToParent(){
    this.getSelectedFolder(this.actualFolder.parentFolder.id);
  }

  reload(){
    this.fileService.initFiles((value:any) => {
      if(value === true){
        //this.setCurrentFolder(); // ### - Share to global fileService
        this.actualFolder = this.fileService.getActualFolder();
        this.files = this.fileService.getFilesInFolder(this.actualFolder.id);
        this.path = this.fileService.getCompletePath();
        console.log(this.actualFolder);
        console.log(this.files);
      }
    });
  }

  openItem(itemId: number){
    let item = this.fileService.getFileById(itemId);
    /*if(item?.fileType.id === 1){
      this.getSelectedFolder(itemId);
    }
    else{

    }*/
    switch(item?.fileType.type){
      case "folder":
        this.getSelectedFolder(itemId);
        break;
      case "audio":
        console.log("this is an audio file!");
        break;
      case "video":
        console.log("this is a video file!");
        break;
      case "image":
        console.log("this is an image!");
        break;
      case "document":
        console.log("this is a document!");
        break;
      case "graphics":
        console.log("this is a graphical file!");
        break;
    }
  }

  // optional
  openFolder(folderId: number){
    this.getSelectedFolder(folderId);
  }

  setCurrentFolder(){
    this.fileService.actualFolder = this.actualFolder;
  }

  emitUpdateView(){
    this.getSelectedFolder(this.actualFolder.id);
  }

  test(){
    console.log("test");
  }

  deleteFile(id: number){
    console.log("delete: "+ id);
    this.fileService.deleteFile(id, (value: any) => {
      if(value){
        console.log("delete folder");
        this.getSelectedFolder(this.actualFolder.id);
      }
    });
  }

  playFile(id: number){
    this.fileService.playFile(id);
  }

  downloadFile(id: number){
    this.fileService.downloadFile(id);
  }


  // ### 1 - SELECT FILE TO UPLOAD
  onFileSelected(event: any) {
    let fileList: FileList = event.target.files;
    //console.log(fileList[0]);
    let file = fileList[0];
    this.selectedFile = file.name; // retrieve file name to display
    this.form.patchValue({
      file: file
    });
    this.form.get('file')?.updateValueAndValidity();
  }

  // ### 2 - UPLOAD FILE
  submitFile(){
    const formData = new FormData();
    const f = this.form.value.file;
    formData.append('file', f);
    this.fileService.uploadFile(formData).subscribe((event: HttpEvent<any>)=>{
      switch (event.type) {
        case HttpEventType.Sent:
          console.log('Request has been made!');
          break;
        case HttpEventType.ResponseHeader:
          console.log('Response header has been received!');
          break;
        case HttpEventType.UploadProgress:
          this.progress = Math.round(event.loaded / event.total! * 100);
          console.log(`Uploaded! ${this.progress}%`);
          break;
        case HttpEventType.Response:
          console.log('User successfully created!', event.body);
          setTimeout(() => {
            this.progress = 0;
          }, 1500)
      }

      this.reload(); // reload dashboard state
      this.selectedFile = ""; // reinit file selection
    })
  }

}
