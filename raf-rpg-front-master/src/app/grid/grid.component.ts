import {Component, HostListener, OnInit} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {ApiCallService} from "../service/api-call.service";
import {forkJoin, interval} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {InventoryDalogComponent} from "../inventory-dalog/inventory-dalog.component";
import {finalize} from "rxjs/operators";
import {OverScreenComponent} from "../over-screen/over-screen.component";

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css'],
})
export class GridComponent implements OnInit {

  constructor(private apiService:ApiCallService, private dialog: MatDialog) { }

  tiles: string[] = [];
  tilesMap = new Map<string,string>([[">","Elevation_Tile"],["|","Gate_Tile"],["$","Mountain_Tile"],
                                            ["_","Pasture_Tile"],["-","Water_Tile"],["+","Wood_Tile"],
                                            [".","Pasture_Tile_Looted"],["<","Elevation_Tile_Looted"],[":","Wood_Tile_Looted"]]);

  private isOver: boolean = false;

  charMap = new Map<string,string>([["B","Barbarian"],["V","Village"],["P","Player_model"], ["M","Merchant"]]);

  number_of_tiles = 4;
  sub: any;
  dialogRef: any = undefined

  ngOnInit(): void {
    this.sub = interval(250).subscribe(() => { this.callGridAPI(); this.callIsOver() });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  callGridAPI(): void {
    this.apiService.callGrid().subscribe((res: string[][]) => {
      this.tiles = res.reduce((acc,val) => acc.concat(val),[]);
      // console.log(this.tiles);
      // console.log(res.length);
      // console.log(res[0].length);
      this.number_of_tiles = res[0].length;
    });
  }

  callIsOver(): void {
    this.apiService.callCheckOver().subscribe((res: boolean) => {
      this.isOver = res;
      if(this.isOver && this.dialogRef == undefined)
        this.openGameOverDialog();
    });
  }

  statusOfAGame(): boolean {
    return !this.isOver;
  }

  openGameOverDialog(): void {
    console.log(this.dialogRef)
    if(this.dialogRef != undefined) {
      this.dialogRef.close();
    }

    this.dialogRef = this.dialog.open(OverScreenComponent, {
      width: '800px',
    })

    this.dialogRef.afterClosed().subscribe( () => {
      this.dialogRef = undefined;
    });
  }

  @HostListener('document:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    const keyCode = event.code;
    console.log(keyCode);
    if (keyCode == "ArrowUp") {
      this.apiService.callUp().subscribe((res: string) => {});
    } else if (keyCode == "ArrowDown") {
      this.apiService.callDown().subscribe((res: string) => {});
    } else if (keyCode == "ArrowLeft") {
      this.apiService.callLeft().subscribe((res: string) => {});
    } else if(keyCode == "ArrowRight") {
      this.apiService.callRight().subscribe((res: string) => {});
    } else if(keyCode == "Space") {
      this.apiService.callWait().subscribe((res: string) => {});
    } else if(keyCode == "ShiftLeft") {
      this.openDialog();
    }

  }

  openDialog(): void {
    console.log(this.dialogRef)
    if(this.dialogRef != undefined) {
      this.dialogRef.close();
      return;
    }

    forkJoin({request1: this.apiService.callInventory(), request2: this.apiService.callGold()}).subscribe(
      ({request1, request2}) => {
        console.log(request1);
        console.log(request2);
        this.dialogRef = this.dialog.open(InventoryDalogComponent, {
          width: '500px',
          data: {inventory: request1,gold: request2}
        })

        this.dialogRef.afterClosed().subscribe( () => {
          this.dialogRef = undefined;
        });
      }
    )

  }


  castToImage(inp: string): string {
    if (inp == " ") {
      return "assets/game tiles/Water_Tile.png";
    }
    if (this.tilesMap.has(inp)) {
      let out = this.tilesMap.get(inp)
      return `assets/game tiles/${out}.png`;
    } else {
      let out = this.charMap.get(inp)
      return `assets/game characters/${out}.png`;
    }
  }

  calculateWidth(): string {

    const tile_width = 65; // You can replace this with your actual height calculation
    return `${tile_width*this.number_of_tiles}px`;
  }

}
