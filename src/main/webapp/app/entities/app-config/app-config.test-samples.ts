import { IAppConfig, NewAppConfig } from './app-config.model';

export const sampleWithRequiredData: IAppConfig = {
  id: 9401,
};

export const sampleWithPartialData: IAppConfig = {
  id: 8225,
  key: 'yahoo pupil',
  value: 'against aha deliquesce',
};

export const sampleWithFullData: IAppConfig = {
  id: 11442,
  key: 'inspect because',
  value: 'whenever',
};

export const sampleWithNewData: NewAppConfig = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
