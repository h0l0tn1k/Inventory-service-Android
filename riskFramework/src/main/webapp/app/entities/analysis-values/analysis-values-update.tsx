import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRiskAssessment } from 'app/shared/model/risk-assessment.model';
import { getEntities as getRiskAssessments } from 'app/entities/risk-assessment/risk-assessment.reducer';
import { getEntity, updateEntity, createEntity, reset } from './analysis-values.reducer';
import { IAnalysisValues } from 'app/shared/model/analysis-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnalysisValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AnalysisValuesUpdate = (props: IAnalysisValuesUpdateProps) => {
  const [riskAssessmentId, setRiskAssessmentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { analysisValuesEntity, riskAssessments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/analysis-values');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRiskAssessments();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...analysisValuesEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="riskFrameworkApp.analysisValues.home.createOrEditLabel">
            <Translate contentKey="riskFrameworkApp.analysisValues.home.createOrEditLabel">Create or edit a AnalysisValues</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : analysisValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="analysis-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="analysis-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="analysis-values-value">
                  <Translate contentKey="riskFrameworkApp.analysisValues.value">Value</Translate>
                </Label>
                <AvField id="analysis-values-value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="analysis-values-riskAssessment">
                  <Translate contentKey="riskFrameworkApp.analysisValues.riskAssessment">Risk Assessment</Translate>
                </Label>
                <AvInput id="analysis-values-riskAssessment" type="select" className="form-control" name="riskAssessment.id">
                  <option value="" key="0" />
                  {riskAssessments
                    ? riskAssessments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/analysis-values" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  riskAssessments: storeState.riskAssessment.entities,
  analysisValuesEntity: storeState.analysisValues.entity,
  loading: storeState.analysisValues.loading,
  updating: storeState.analysisValues.updating,
  updateSuccess: storeState.analysisValues.updateSuccess,
});

const mapDispatchToProps = {
  getRiskAssessments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AnalysisValuesUpdate);
