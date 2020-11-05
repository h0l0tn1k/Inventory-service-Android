export interface IRiskTemplate {
  id?: number;
  name?: string;
  description?: string;
  riskDescription?: string;
  assessmentDescription?: string;
}

export const defaultValue: Readonly<IRiskTemplate> = {};
