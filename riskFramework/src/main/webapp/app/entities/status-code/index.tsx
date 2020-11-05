import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StatusCode from './status-code';
import StatusCodeDetail from './status-code-detail';
import StatusCodeUpdate from './status-code-update';
import StatusCodeDeleteDialog from './status-code-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StatusCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StatusCodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StatusCodeDetail} />
      <ErrorBoundaryRoute path={match.url} component={StatusCode} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StatusCodeDeleteDialog} />
  </>
);

export default Routes;
