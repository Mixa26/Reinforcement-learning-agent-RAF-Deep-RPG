import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverScreenComponent } from './over-screen.component';

describe('OverScreenComponent', () => {
  let component: OverScreenComponent;
  let fixture: ComponentFixture<OverScreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverScreenComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
