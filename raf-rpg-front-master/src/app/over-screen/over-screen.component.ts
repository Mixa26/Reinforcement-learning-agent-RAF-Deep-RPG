import { Component, OnInit } from '@angular/core';
import {ApiCallService} from "../service/api-call.service";

@Component({
  selector: 'app-over-screen',
  templateUrl: './over-screen.component.html',
  styleUrls: ['./over-screen.component.css']
})
export class OverScreenComponent implements OnInit {

  constructor(private apiService:ApiCallService) { }

  ngOnInit(): void {
  }

  restartGame(): void {
    this.apiService.callRestart().subscribe((res: boolean) => {
      if(res)
        console.log("Restarted game");
      else
        console.log("Failed to restart game");
    });
  }

}
