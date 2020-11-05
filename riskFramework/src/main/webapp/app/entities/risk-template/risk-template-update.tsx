import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './risk-template.reducer';
import { IRiskTemplate } from 'app/shared/model/risk-template.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRiskTemplateUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RiskTemplateUpdate = (props: IRiskTemplateUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { riskTemplateEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/risk-template');
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
        ...riskTemplateEntity,
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
          <h2 id="riskFrameworkApp.riskTemplate.home.createOrEditLabel">
            <Translate contentKey="riskFrameworkApp.riskTemplate.home.createOrEditLabel">Create or edit a RiskTemplate</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : riskTemplateEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="risk-template-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="risk-template-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="risk-template-name">
                  <Translate contentKey="riskFrameworkApp.riskTemplate.name">Name</Translate>
                </Label>
                <AvField
                  id="risk-template-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="risk-template-description">
                  <Translate contentKey="riskFrameworkApp.riskTemplate.description">Description</Translate>
                </Label>
                <AvField id="risk-template-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="riskDescriptionLabel" for="risk-template-riskDescription">
                  <Translate contentKey="riskFrameworkApp.riskTemplate.riskDescription">Risk Description</Translate>
                </Label>
                <AvField id="risk-template-riskDescription" type="text" name="riskDescription" />
              </AvGroup>
              <AvGroup>
                <Label id="assessmentDescriptionLabel" for="risk-template-assessmentDescription">
                  <Translate contentKey="riskFrameworkApp.riskTemplate.assessmentDescription">Assessment Description</Translate>
                </Label>
                <AvField id="risk-template-assessmentDescription" type="text" name="assessmentDescription" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/risk-template" replace color="info">
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
  riskTemplateEntity: storeState.riskTemplate.entity,
  loading: storeState.riskTemplate.loading,
  updating: storeState.riskTemplate.updating,
  updateSuccess: storeState.riskTemplate.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RiskTemplateUpdate);
