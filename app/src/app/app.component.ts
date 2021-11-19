import { Component, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'filexio-app';
  isAuth: boolean = false;
  baseUrl = 'http://localhost:3009';
}
