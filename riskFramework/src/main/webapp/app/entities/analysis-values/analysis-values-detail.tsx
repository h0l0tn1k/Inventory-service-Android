import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './analysis-values.reducer';
import { IAnalysisValues } from 'app/shared/model/analysis-values.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnalysisValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnalysisValuesDetail = (props: IAnalysisValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { analysisValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="riskFrameworkApp.analysisValues.detail.title">AnalysisValues</Translate> [<b>{analysisValuesEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="value">
              <Translate contentKey="riskFrameworkApp.analysisValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{analysisValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="riskFrameworkApp.analysisValues.riskAssessment">Risk Assessment</Translate>
          </dt>
          <dd>{analysisValuesEntity.riskAssessment ? analysisValuesEntity.riskAssessment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/analysis-values" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/analysis-values/${analysisValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ analysisValues }: IRootState) => ({
  analysisValuesEntity: analysisValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnalysisValuesDetail);
