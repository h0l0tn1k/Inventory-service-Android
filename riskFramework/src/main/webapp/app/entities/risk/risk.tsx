import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './risk.reducer';
import { IRisk } from 'app/shared/model/risk.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRiskProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Risk = (props: IRiskProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { riskList, match, loading } = props;
  return (
    <div>
      <h2 id="risk-heading">
        <Translate contentKey="riskFrameworkApp.risk.home.title">Risks</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="riskFrameworkApp.risk.home.createLabel">Create new Risk</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {riskList && riskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.risk.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.risk.version">Version</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.risk.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.risk.validFrom">Valid From</Translate>
                </th>
                <th>
                  <Translate contentKey="riskFrameworkApp.risk.validTo">Valid To</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {riskList.map((risk, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${risk.id}`} color="link" size="sm">
                      {risk.id}
                    </Button>
                  </td>
                  <td>{risk.name}</td>
                  <td>{risk.version}</td>
                  <td>{risk.description}</td>
                  <td>{risk.validFrom ? <TextFormat type="date" value={risk.validFrom} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{risk.validTo ? <TextFormat type="date" value={risk.validTo} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${risk.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${risk.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${risk.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="riskFrameworkApp.risk.home.notFound">No Risks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ risk }: IRootState) => ({
  riskList: risk.entities,
  loading: risk.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Risk);
