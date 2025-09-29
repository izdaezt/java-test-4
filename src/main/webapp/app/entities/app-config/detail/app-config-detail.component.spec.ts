import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AppConfigDetailComponent } from './app-config-detail.component';

describe('AppConfig Management Detail Component', () => {
  let comp: AppConfigDetailComponent;
  let fixture: ComponentFixture<AppConfigDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppConfigDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./app-config-detail.component').then(m => m.AppConfigDetailComponent),
              resolve: { appConfig: () => of({ id: 10896 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AppConfigDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppConfigDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load appConfig on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AppConfigDetailComponent);

      // THEN
      expect(instance.appConfig()).toEqual(expect.objectContaining({ id: 10896 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
