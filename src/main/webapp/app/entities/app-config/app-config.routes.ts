import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AppConfigResolve from './route/app-config-routing-resolve.service';

const appConfigRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/app-config.component').then(m => m.AppConfigComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/app-config-detail.component').then(m => m.AppConfigDetailComponent),
    resolve: {
      appConfig: AppConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/app-config-update.component').then(m => m.AppConfigUpdateComponent),
    resolve: {
      appConfig: AppConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/app-config-update.component').then(m => m.AppConfigUpdateComponent),
    resolve: {
      appConfig: AppConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appConfigRoute;
