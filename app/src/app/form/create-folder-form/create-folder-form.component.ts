import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { FileService } from 'src/app/service/file.service';

@Component({
  selector: 'app-create-folder-form',
  templateUrl: './create-folder-form.component.html',
  styleUrls: ['./create-folder-form.component.css']
})
export class CreateFolderFormComponent implements OnInit {

  @Output() closeModal: EventEmitter<any> = new EventEmitter();
  @Output() reloadData: EventEmitter<any> = new EventEmitter();
  constructor(private fileService: FileService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(formDatas: NgForm){
    console.log(formDatas.value);
      this.fileService.createFolder(formDatas.value['folder'], (data:any) => {
        if(data){
          this.closeModal.emit(true);
          this.reloadData.emit(true);
        }
      });
  }

}
