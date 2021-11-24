import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardFirstComponent } from './dashboard-first.component';

describe('DashboardFirstComponent', () => {
  let component: DashboardFirstComponent;
  let fixture: ComponentFixture<DashboardFirstComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardFirstComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardFirstComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
