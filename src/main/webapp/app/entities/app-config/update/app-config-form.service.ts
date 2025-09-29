import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAppConfig, NewAppConfig } from '../app-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppConfig for edit and NewAppConfigFormGroupInput for create.
 */
type AppConfigFormGroupInput = IAppConfig | PartialWithRequiredKeyOf<NewAppConfig>;

type AppConfigFormDefaults = Pick<NewAppConfig, 'id'>;

type AppConfigFormGroupContent = {
  id: FormControl<IAppConfig['id'] | NewAppConfig['id']>;
  key: FormControl<IAppConfig['key']>;
  value: FormControl<IAppConfig['value']>;
};

export type AppConfigFormGroup = FormGroup<AppConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppConfigFormService {
  createAppConfigFormGroup(appConfig: AppConfigFormGroupInput = { id: null }): AppConfigFormGroup {
    const appConfigRawValue = {
      ...this.getFormDefaults(),
      ...appConfig,
    };
    return new FormGroup<AppConfigFormGroupContent>({
      id: new FormControl(
        { value: appConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      key: new FormControl(appConfigRawValue.key),
      value: new FormControl(appConfigRawValue.value),
    });
  }

  getAppConfig(form: AppConfigFormGroup): IAppConfig | NewAppConfig {
    return form.getRawValue() as IAppConfig | NewAppConfig;
  }

  resetForm(form: AppConfigFormGroup, appConfig: AppConfigFormGroupInput): void {
    const appConfigRawValue = { ...this.getFormDefaults(), ...appConfig };
    form.reset(
      {
        ...appConfigRawValue,
        id: { value: appConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppConfigFormDefaults {
    return {
      id: null,
    };
  }
}
