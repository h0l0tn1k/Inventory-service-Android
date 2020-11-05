import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './risk-assessment.reducer';
import { IRiskAssessment } from 'app/shared/model/risk-assessment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRiskAssessmentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RiskAssessmentUpdate = (props: IRiskAssessmentUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { riskAssessmentEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/risk-assessment');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...riskAssessmentEntity,
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
          <h2 id="riskFrameworkApp.riskAssessment.home.createOrEditLabel">
            <Translate contentKey="riskFrameworkApp.riskAssessment.home.createOrEditLabel">Create or edit a RiskAssessment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : riskAssessmentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="risk-assessment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="risk-assessment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="risk-assessment-name">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.name">Name</Translate>
                </Label>
                <AvField
                  id="risk-assessment-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="versionLabel" for="risk-assessment-version">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.version">Version</Translate>
                </Label>
                <AvField
                  id="risk-assessment-version"
                  type="string"
                  className="form-control"
                  name="version"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="risk-assessment-description">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.description">Description</Translate>
                </Label>
                <AvField
                  id="risk-assessment-description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="validFromLabel" for="risk-assessment-validFrom">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.validFrom">Valid From</Translate>
                </Label>
                <AvField
                  id="risk-assessment-validFrom"
                  type="date"
                  className="form-control"
                  name="validFrom"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="validToLabel" for="risk-assessment-validTo">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.validTo">Valid To</Translate>
                </Label>
                <AvField
                  id="risk-assessment-validTo"
                  type="date"
                  className="form-control"
                  name="validTo"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="riskIdLabel" for="risk-assessment-riskId">
                  <Translate contentKey="riskFrameworkApp.riskAssessment.riskId">Risk Id</Translate>
                </Label>
                <AvField
                  id="risk-assessment-riskId"
                  type="text"
                  name="riskId"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/risk-assessment" replace color="info">
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
  riskAssessmentEntity: storeState.riskAssessment.entity,
  loading: storeState.riskAssessment.loading,
  updating: storeState.riskAssessment.updating,
  updateSuccess: storeState.riskAssessment.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RiskAssessmentUpdate);
