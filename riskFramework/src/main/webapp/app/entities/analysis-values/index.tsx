import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnalysisValues from './analysis-values';
import AnalysisValuesDetail from './analysis-values-detail';
import AnalysisValuesUpdate from './analysis-values-update';
import AnalysisValuesDeleteDialog from './analysis-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnalysisValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnalysisValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnalysisValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnalysisValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnalysisValuesDeleteDialog} />
  </>
);

export default Routes;
