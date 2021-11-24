import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardTwiceComponent } from './dashboard-twice.component';

describe('DashboardTwiceComponent', () => {
  let component: DashboardTwiceComponent;
  let fixture: ComponentFixture<DashboardTwiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardTwiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardTwiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
