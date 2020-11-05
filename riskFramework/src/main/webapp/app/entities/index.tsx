import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StatusCode from './status-code';
import Risk from './risk';
import RiskAssessment from './risk-assessment';
import AnalysisValues from './analysis-values';
import RiskTemplate from './risk-template';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}status-code`} component={StatusCode} />
      <ErrorBoundaryRoute path={`${match.url}risk`} component={Risk} />
      <ErrorBoundaryRoute path={`${match.url}risk-assessment`} component={RiskAssessment} />
      <ErrorBoundaryRoute path={`${match.url}analysis-values`} component={AnalysisValues} />
      <ErrorBoundaryRoute path={`${match.url}risk-template`} component={RiskTemplate} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
