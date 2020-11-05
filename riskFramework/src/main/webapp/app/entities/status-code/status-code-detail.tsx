import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './status-code.reducer';
import { IStatusCode } from 'app/shared/model/status-code.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStatusCodeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StatusCodeDetail = (props: IStatusCodeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { statusCodeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="riskFrameworkApp.statusCode.detail.title">StatusCode</Translate> [<b>{statusCodeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="code">
              <Translate contentKey="riskFrameworkApp.statusCode.code">Code</Translate>
            </span>
          </dt>
          <dd>{statusCodeEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/status-code" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/status-code/${statusCodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ statusCode }: IRootState) => ({
  statusCodeEntity: statusCode.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StatusCodeDetail);
