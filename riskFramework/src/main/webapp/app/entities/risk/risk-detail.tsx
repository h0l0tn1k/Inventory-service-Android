import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './risk.reducer';
import { IRisk } from 'app/shared/model/risk.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRiskDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RiskDetail = (props: IRiskDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { riskEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="riskFrameworkApp.risk.detail.title">Risk</Translate> [<b>{riskEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="riskFrameworkApp.risk.name">Name</Translate>
            </span>
          </dt>
          <dd>{riskEntity.name}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="riskFrameworkApp.risk.version">Version</Translate>
            </span>
          </dt>
          <dd>{riskEntity.version}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="riskFrameworkApp.risk.description">Description</Translate>
            </span>
          </dt>
          <dd>{riskEntity.description}</dd>
          <dt>
            <span id="validFrom">
              <Translate contentKey="riskFrameworkApp.risk.validFrom">Valid From</Translate>
            </span>
          </dt>
          <dd>{riskEntity.validFrom ? <TextFormat value={riskEntity.validFrom} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="validTo">
              <Translate contentKey="riskFrameworkApp.risk.validTo">Valid To</Translate>
            </span>
          </dt>
          <dd>{riskEntity.validTo ? <TextFormat value={riskEntity.validTo} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/risk" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/risk/${riskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ risk }: IRootState) => ({
  riskEntity: risk.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RiskDetail);
