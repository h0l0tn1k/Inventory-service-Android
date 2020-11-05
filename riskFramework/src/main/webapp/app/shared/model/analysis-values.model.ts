import { IRiskAssessment } from 'app/shared/model/risk-assessment.model';

export interface IAnalysisValues {
  id?: number;
  value?: string;
  riskAssessment?: IRiskAssessment;
}

export const defaultValue: Readonly<IAnalysisValues> = {};
