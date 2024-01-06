import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiCallService {

  private readonly apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {
    console.log('ApiCallService constructor called');
  }

  private static handleError(error:HttpErrorResponse){
    try {
      console.log('lol', error.error['msg'])
      return throwError(error.error['msg'])
    }catch (gre){
      return throwError(error.error)
    }
  }

  callGrid(): any{
    // console.log('ApiCallService callGrid called');
    return this.http.get(this.apiUrl + '/map/full/matrix').pipe(catchError(ApiCallService.handleError));
  }

  callCheckOver(): any{
    // console.log('ApiCallService callGrid called');
    return this.http.get(this.apiUrl + '/map/isover').pipe(catchError(ApiCallService.handleError));
  }

  private getRandomInt(max:number): number {
    return Math.floor(Math.random() * max);
  }

  callRestart(): any{
    let val = this.getRandomInt(5);
    console.log(`ApiCallService RESTART GAME ${val} called`);
    return this.http.put(this.apiUrl + `/map/restart?map_number=${val}`, {}, {headers: new HttpHeaders().set("Content-Type","application/json")
        .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callUp(): any {
    console.log('ApiCallService UP called');
    return this.http.put(this.apiUrl + '/player/up', {}, {headers: new HttpHeaders().set("Content-Type","application/json")
        .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callDown(): any {
    console.log('ApiCallService DOWN called');
    return this.http.put(this.apiUrl + '/player/down', {}, {headers: new HttpHeaders().set("Content-Type","application/json")
        .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callLeft(): any {
    console.log('ApiCallService LEFT called');
    return this.http.put(this.apiUrl + '/player/left', {}, {headers: new HttpHeaders().set("Content-Type","application/json")
        .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callRight(): any {
    console.log('ApiCallService RIGHT called');
    return this.http.put(this.apiUrl + '/player/right', {}, {headers: new HttpHeaders().set("Content-Type","application/json")
      .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callWait(): any {
    console.log('ApiCallService WAIT called');
    return this.http.put(this.apiUrl + '/player/wait', {}, {headers: new HttpHeaders().set("Content-Type","application/json")
        .set("Access-Control-Allow-Private-Network","true")}).pipe(catchError(ApiCallService.handleError));
  }

  callInventory(): any {
    console.log('ApiCallService INVENTORY called');
    return this.http.get(this.apiUrl + '/player/inventory').pipe(catchError(ApiCallService.handleError));
  }

  callGold(): any {
    console.log('ApiCallService INVENTORY GOLD called');
    return this.http.get(this.apiUrl + '/player/inventory/gold').pipe(catchError(ApiCallService.handleError));
  }

}
