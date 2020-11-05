import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './status-code.reducer';
import { IStatusCode } from 'app/shared/model/status-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStatusCodeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const StatusCode = (props: IStatusCodeProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { statusCodeList, match, loading } = props;
  return (
    <div>
      <h2 id="status-code-heading">
        <Translate contentKey="riskFrameworkApp.statusCode.home.title">Status Codes</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="riskFrameworkApp.statusCode.home.createLabel">Create new Status Code</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {statusCodeList && statusCodeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.statusCode.code">Code</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {statusCodeList.map((statusCode, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${statusCode.id}`} color="link" size="sm">
                      {statusCode.id}
                    </Button>
                  </td>
                  <td>{statusCode.code}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${statusCode.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${statusCode.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${statusCode.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="riskFrameworkApp.statusCode.home.notFound">No Status Codes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ statusCode }: IRootState) => ({
  statusCodeList: statusCode.entities,
  loading: statusCode.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StatusCode);
