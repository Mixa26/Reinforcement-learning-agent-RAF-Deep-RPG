import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {InventoryDialogData} from "../models/data";

@Component({
  selector: 'app-inventory-dalog',
  templateUrl: './inventory-dalog.component.html',
  styleUrls: ['./inventory-dalog.component.css']
})
export class InventoryDalogComponent implements OnInit {

  inventorySize:number = 4;

  constructor(@Inject(MAT_DIALOG_DATA) public input:InventoryDialogData) {
    // console.log("inventory size Š0Đ: " + input.inventory.size)
    //
    // this.inventorySize = Object.keys(input.inventory).length ;
    // console.log(input.gold)
    // console.log("inventory size: " + this.inventorySize)
  }


  castToImage(key:string):string {
    return `assets/game loot/${key}.png`
  }

  ngOnInit(): void {
  }

}
