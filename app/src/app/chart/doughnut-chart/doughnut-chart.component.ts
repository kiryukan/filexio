import { Component, OnInit } from '@angular/core';
import { ChartType } from 'chart.js';
import { MultiDataSet, Label } from 'ng2-charts';
import { FileService } from 'src/app/service/file.service';
import { File } from 'src/app/model/file.model';
import { FileType } from 'src/app/model/filetype.model';
import { FileTypeService } from 'src/app/service/file-type.service';
import { iif } from 'rxjs';

@Component({
  selector: 'app-doughnut-chart',
  templateUrl: './doughnut-chart.component.html',
  styleUrls: ['./doughnut-chart.component.css']
})
export class DoughnutChartComponent implements OnInit {

  files: File[] = [];
  /*doughnutChartLabels: Label[] = ['BMW', 'Ford', 'Tesla'];
  doughnutChartData: MultiDataSet = [
    [55, 25, 20]
  ];*/
  doughnutChartLabels: Label[] = [];
  doughnutChartData: MultiDataSet = [[]];
  doughnutChartType: ChartType = 'doughnut';

  constructor(private fileService: FileService, private fileTypeService: FileTypeService) { 
    this.files = this.fileService.files;
    this.setChartLabel(this.files);
  }

  ngOnInit(): void {
  }

  setChartLabel(arrayFiles: File[]){
    let filesTmp = this.files;
    let counter = 0;
    for(let ftmp of filesTmp){
      if(ftmp.fileType.type === "folder"){
        let index = filesTmp.indexOf(ftmp);
        filesTmp.splice(index, 0);
      }
      else
      {
        let foundLabel = this.doughnutChartLabels.find((f) => {
          return f === ftmp.fileType.type;
        });
        if(foundLabel !== ftmp.fileType.type && foundLabel !== "folder"){
          this.doughnutChartLabels.push(ftmp.fileType.type);
          for(let file of filesTmp){
            if(file.fileType.type === ftmp.fileType.type){
              ++counter;
            }
          }
          this.doughnutChartData[0].push(counter);
          counter = 0;
        }
      }
    }
  }

}
