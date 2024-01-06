import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryDalogComponent } from './inventory-dalog.component';

describe('InventoryDalogComponent', () => {
  let component: InventoryDalogComponent;
  let fixture: ComponentFixture<InventoryDalogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InventoryDalogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InventoryDalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
