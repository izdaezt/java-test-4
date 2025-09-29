export interface IAppConfig {
  id: number;
  key?: string | null;
  value?: string | null;
}

export type NewAppConfig = Omit<IAppConfig, 'id'> & { id: null };
