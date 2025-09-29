import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAppConfig } from '../app-config.model';

@Component({
  selector: 'jhi-app-config-detail',
  templateUrl: './app-config-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AppConfigDetailComponent {
  appConfig = input<IAppConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
