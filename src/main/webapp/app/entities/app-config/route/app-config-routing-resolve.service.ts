import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppConfig } from '../app-config.model';
import { AppConfigService } from '../service/app-config.service';

const appConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppConfig> => {
  const id = route.params.id;
  if (id) {
    return inject(AppConfigService)
      .find(id)
      .pipe(
        mergeMap((appConfig: HttpResponse<IAppConfig>) => {
          if (appConfig.body) {
            return of(appConfig.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default appConfigResolve;
