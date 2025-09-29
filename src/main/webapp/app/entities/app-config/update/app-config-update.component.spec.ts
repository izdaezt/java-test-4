import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AppConfigService } from '../service/app-config.service';
import { IAppConfig } from '../app-config.model';
import { AppConfigFormService } from './app-config-form.service';

import { AppConfigUpdateComponent } from './app-config-update.component';

describe('AppConfig Management Update Component', () => {
  let comp: AppConfigUpdateComponent;
  let fixture: ComponentFixture<AppConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appConfigFormService: AppConfigFormService;
  let appConfigService: AppConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppConfigUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AppConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appConfigFormService = TestBed.inject(AppConfigFormService);
    appConfigService = TestBed.inject(AppConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const appConfig: IAppConfig = { id: 7808 };

      activatedRoute.data = of({ appConfig });
      comp.ngOnInit();

      expect(comp.appConfig).toEqual(appConfig);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppConfig>>();
      const appConfig = { id: 10896 };
      jest.spyOn(appConfigFormService, 'getAppConfig').mockReturnValue(appConfig);
      jest.spyOn(appConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appConfig }));
      saveSubject.complete();

      // THEN
      expect(appConfigFormService.getAppConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appConfigService.update).toHaveBeenCalledWith(expect.objectContaining(appConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppConfig>>();
      const appConfig = { id: 10896 };
      jest.spyOn(appConfigFormService, 'getAppConfig').mockReturnValue({ id: null });
      jest.spyOn(appConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appConfig }));
      saveSubject.complete();

      // THEN
      expect(appConfigFormService.getAppConfig).toHaveBeenCalled();
      expect(appConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppConfig>>();
      const appConfig = { id: 10896 };
      jest.spyOn(appConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
