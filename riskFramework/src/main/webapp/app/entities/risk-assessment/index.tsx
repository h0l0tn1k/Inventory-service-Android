import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RiskAssessment from './risk-assessment';
import RiskAssessmentDetail from './risk-assessment-detail';
import RiskAssessmentUpdate from './risk-assessment-update';
import RiskAssessmentDeleteDialog from './risk-assessment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RiskAssessmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RiskAssessmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RiskAssessmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={RiskAssessment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RiskAssessmentDeleteDialog} />
  </>
);

export default Routes;
