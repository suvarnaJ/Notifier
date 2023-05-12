import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserVerifyPasswordComponent } from './user-verify-password.component';

describe('UserVerifyPasswordComponent', () => {
  let component: UserVerifyPasswordComponent;
  let fixture: ComponentFixture<UserVerifyPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserVerifyPasswordComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserVerifyPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
