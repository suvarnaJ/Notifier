import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTemplateComponent } from './show-template.component';

describe('ShowTemplateComponent', () => {
  let component: ShowTemplateComponent;
  let fixture: ComponentFixture<ShowTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowTemplateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
