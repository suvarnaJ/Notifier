import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportUserdefinedTemplateComponent } from './import-userdefined-template.component';

describe('ImportUserdefinedTemplateComponent', () => {
  let component: ImportUserdefinedTemplateComponent;
  let fixture: ComponentFixture<ImportUserdefinedTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportUserdefinedTemplateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImportUserdefinedTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
