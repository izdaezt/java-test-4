import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'inmobiJavaTest4App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'app-config',
    data: { pageTitle: 'inmobiJavaTest4App.appConfig.home.title' },
    loadChildren: () => import('./app-config/app-config.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
