import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppConfig } from '../app-config.model';
import { AppConfigService } from '../service/app-config.service';
import { AppConfigFormGroup, AppConfigFormService } from './app-config-form.service';

@Component({
  selector: 'jhi-app-config-update',
  templateUrl: './app-config-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppConfigUpdateComponent implements OnInit {
  isSaving = false;
  appConfig: IAppConfig | null = null;

  protected appConfigService = inject(AppConfigService);
  protected appConfigFormService = inject(AppConfigFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppConfigFormGroup = this.appConfigFormService.createAppConfigFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appConfig }) => {
      this.appConfig = appConfig;
      if (appConfig) {
        this.updateForm(appConfig);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appConfig = this.appConfigFormService.getAppConfig(this.editForm);
    if (appConfig.id !== null) {
      this.subscribeToSaveResponse(this.appConfigService.update(appConfig));
    } else {
      this.subscribeToSaveResponse(this.appConfigService.create(appConfig));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppConfig>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appConfig: IAppConfig): void {
    this.appConfig = appConfig;
    this.appConfigFormService.resetForm(this.editForm, appConfig);
  }
}
