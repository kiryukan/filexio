import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFolderFormComponent } from './create-folder-form.component';

describe('CreateFolderFormComponent', () => {
  let component: CreateFolderFormComponent;
  let fixture: ComponentFixture<CreateFolderFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateFolderFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateFolderFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
