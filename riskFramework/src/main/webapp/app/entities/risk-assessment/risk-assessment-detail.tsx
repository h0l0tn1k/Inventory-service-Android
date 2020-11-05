import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './risk-assessment.reducer';
import { IRiskAssessment } from 'app/shared/model/risk-assessment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRiskAssessmentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RiskAssessmentDetail = (props: IRiskAssessmentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { riskAssessmentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="riskFrameworkApp.riskAssessment.detail.title">RiskAssessment</Translate> [<b>{riskAssessmentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="riskFrameworkApp.riskAssessment.name">Name</Translate>
            </span>
          </dt>
          <dd>{riskAssessmentEntity.name}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="riskFrameworkApp.riskAssessment.version">Version</Translate>
            </span>
          </dt>
          <dd>{riskAssessmentEntity.version}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="riskFrameworkApp.riskAssessment.description">Description</Translate>
            </span>
          </dt>
          <dd>{riskAssessmentEntity.description}</dd>
          <dt>
            <span id="validFrom">
              <Translate contentKey="riskFrameworkApp.riskAssessment.validFrom">Valid From</Translate>
            </span>
          </dt>
          <dd>
            {riskAssessmentEntity.validFrom ? (
              <TextFormat value={riskAssessmentEntity.validFrom} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="validTo">
              <Translate contentKey="riskFrameworkApp.riskAssessment.validTo">Valid To</Translate>
            </span>
          </dt>
          <dd>
            {riskAssessmentEntity.validTo ? (
              <TextFormat value={riskAssessmentEntity.validTo} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="riskId">
              <Translate contentKey="riskFrameworkApp.riskAssessment.riskId">Risk Id</Translate>
            </span>
          </dt>
          <dd>{riskAssessmentEntity.riskId}</dd>
        </dl>
        <Button tag={Link} to="/risk-assessment" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/risk-assessment/${riskAssessmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ riskAssessment }: IRootState) => ({
  riskAssessmentEntity: riskAssessment.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RiskAssessmentDetail);
