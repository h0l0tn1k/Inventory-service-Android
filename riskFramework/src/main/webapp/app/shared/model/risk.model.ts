import { Moment } from 'moment';

export interface IRisk {
  id?: number;
  name?: string;
  version?: number;
  description?: string;
  validFrom?: string;
  validTo?: string;
}

export const defaultValue: Readonly<IRisk> = {};
