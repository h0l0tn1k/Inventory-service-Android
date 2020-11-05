import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRiskAssessment, defaultValue } from 'app/shared/model/risk-assessment.model';

export const ACTION_TYPES = {
  FETCH_RISKASSESSMENT_LIST: 'riskAssessment/FETCH_RISKASSESSMENT_LIST',
  FETCH_RISKASSESSMENT: 'riskAssessment/FETCH_RISKASSESSMENT',
  CREATE_RISKASSESSMENT: 'riskAssessment/CREATE_RISKASSESSMENT',
  UPDATE_RISKASSESSMENT: 'riskAssessment/UPDATE_RISKASSESSMENT',
  DELETE_RISKASSESSMENT: 'riskAssessment/DELETE_RISKASSESSMENT',
  RESET: 'riskAssessment/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRiskAssessment>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type RiskAssessmentState = Readonly<typeof initialState>;

// Reducer

export default (state: RiskAssessmentState = initialState, action): RiskAssessmentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RISKASSESSMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RISKASSESSMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_RISKASSESSMENT):
    case REQUEST(ACTION_TYPES.UPDATE_RISKASSESSMENT):
    case REQUEST(ACTION_TYPES.DELETE_RISKASSESSMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_RISKASSESSMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RISKASSESSMENT):
    case FAILURE(ACTION_TYPES.CREATE_RISKASSESSMENT):
    case FAILURE(ACTION_TYPES.UPDATE_RISKASSESSMENT):
    case FAILURE(ACTION_TYPES.DELETE_RISKASSESSMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RISKASSESSMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RISKASSESSMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_RISKASSESSMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_RISKASSESSMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_RISKASSESSMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/risk-assessments';

// Actions

export const getEntities: ICrudGetAllAction<IRiskAssessment> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RISKASSESSMENT_LIST,
  payload: axios.get<IRiskAssessment>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IRiskAssessment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RISKASSESSMENT,
    payload: axios.get<IRiskAssessment>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRiskAssessment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RISKASSESSMENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRiskAssessment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RISKASSESSMENT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRiskAssessment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RISKASSESSMENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
