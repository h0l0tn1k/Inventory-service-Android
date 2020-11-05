import { Moment } from 'moment';
import { IAnalysisValues } from 'app/shared/model/analysis-values.model';

export interface IRiskAssessment {
  id?: number;
  name?: string;
  version?: number;
  description?: string;
  validFrom?: string;
  validTo?: string;
  riskId?: string;
  analysisValues?: IAnalysisValues[];
}

export const defaultValue: Readonly<IRiskAssessment> = {};
