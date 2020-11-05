import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import statusCode, {
  StatusCodeState
} from 'app/entities/status-code/status-code.reducer';
// prettier-ignore
import risk, {
  RiskState
} from 'app/entities/risk/risk.reducer';
// prettier-ignore
import riskAssessment, {
  RiskAssessmentState
} from 'app/entities/risk-assessment/risk-assessment.reducer';
// prettier-ignore
import analysisValues, {
  AnalysisValuesState
} from 'app/entities/analysis-values/analysis-values.reducer';
// prettier-ignore
import riskTemplate, {
  RiskTemplateState
} from 'app/entities/risk-template/risk-template.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly statusCode: StatusCodeState;
  readonly risk: RiskState;
  readonly riskAssessment: RiskAssessmentState;
  readonly analysisValues: AnalysisValuesState;
  readonly riskTemplate: RiskTemplateState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  statusCode,
  risk,
  riskAssessment,
  analysisValues,
  riskTemplate,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
