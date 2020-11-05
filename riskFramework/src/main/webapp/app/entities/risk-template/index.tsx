import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RiskTemplate from './risk-template';
import RiskTemplateDetail from './risk-template-detail';
import RiskTemplateUpdate from './risk-template-update';
import RiskTemplateDeleteDialog from './risk-template-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RiskTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RiskTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RiskTemplateDetail} />
      <ErrorBoundaryRoute path={match.url} component={RiskTemplate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RiskTemplateDeleteDialog} />
  </>
);

export default Routes;
